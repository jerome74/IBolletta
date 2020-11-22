package com.wlp.ibolletta.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.face.FaceDetector
import com.wlp.ibolletta.R
import com.wlp.ibolletta.models.Bolletta
import com.wlp.ibolletta.util.*
import kotlinx.android.synthetic.main.activity_ocr.*
import kotlinx.android.synthetic.main.bolletta_item_list.*

class OCRActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)


    lateinit var tessOCR : TessOCR
    lateinit var result : String

    var edit =  Editable.Factory.getInstance()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setSupportActionBar(toolbar_ocr);
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)


        tessOCR = TessOCR(this@OCRActivity)

        val taskOCR = TaskOCR({
            try {
                runOnUiThread({
                    custom_ocr_tv.visibility = View.VISIBLE
                    custom_ocr_tv.text = " ${getString(R.string.elab_ocr)} "
                })
                //barcode()
                ocr()
            }catch (e : Exception){
                Log.e("ERROR", e.localizedMessage , e)
            }

        },{
            try {
                runOnUiThread({
                    custom_ocr_tv.text = ""
                    custom_ocr_tv.visibility = View.INVISIBLE

                    postOcr()
                })

            }catch (e : Exception){
                Log.e("ERROR", e.localizedMessage , e)
            }

        }, "taskOCR")

        taskOCR.start()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun ocr() {
        result = tessOCR.getOCRResult(ScannerizationActivity.bitScannerization!!);
    }

    fun barcode(){
        val faceDetector = FaceDetector.Builder(this).setTrackingEnabled(false).build();

        if (!faceDetector.isOperational()) {
           Log.i("INFO", "Could not set up the detector!");
        }
        else{
            val frame = Frame.Builder().setBitmap(ScannerizationActivity.bitScannerization!!).build();
            val barcodes = faceDetector.detect(frame);
            val code = barcodes.valueAt(0);
            val result = code.id;

            Log.i("INFO", "Could not set up the detector!");

        }
    }

    fun postOcr()
    {
       if(result.length > 0){

           ocr_result_tv.text = result

           val parseORCBolletta = ParseORCBolletta(result)
           parseORCBolletta.parseNumberBarCode()

           import_value.text = edit.newEditable(parseORCBolletta.importo.trimStart('0'))
           number_value.text = edit.newEditable(parseORCBolletta.numero.trimStart('0'))
           cc_value.text = edit.newEditable(parseORCBolletta.cc.trimStart('0'))
           td_value.text = edit.newEditable(parseORCBolletta.td.trimStart('0'))

           scad_value.text = edit.newEditable(parseORCBolletta.getScadenza())
       }
        else{
           ocr_result_tv.text = getString(R.string.no_elab_ocr)
       }
    }

    fun onSaveStepOcrClicked(viev : View){

        if(cc_value.text.isBlank() && (
                    import_value.text.isBlank() ||
                    scad_value.text.isBlank() ||
                    number_value.text.isBlank() ||
                    owner_value.text.isBlank() ||
                    td_value.text.isBlank()
                    )){
            ToastCustom.show(this@OCRActivity, getString(R.string.no_valid_ocr))
        }

        cc_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_cc_value))
            return }

        import_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_input_value))
            return }

        scad_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_scad_value))
            return }

        number_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_number_value))
            return }

        owner_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_owner_value))
            return }

        td_value.text.ifBlank { ToastCustom.show(this@OCRActivity, getString(R.string.no_td_value))
            return }


        val bolletta = Bolletta("",cc_value.text.toString()
            ,import_value.text.toString()
            ,scad_value.text.toString()
            ,number_value.text.toString()
            ,owner_value.text.toString()
            ,td_value.text.toString())

        val saveBollettaIntent = Intent(BROADCAST_SAVE_BOLLETTA)

        try {
            saveBollettaIntent.putExtra("BOLLETTA", bolletta)
        }catch (e : java.lang.Exception){
            Log.e("Error :", e.message, e)
        }

        LocalBroadcastManager.getInstance(this@OCRActivity).sendBroadcast(saveBollettaIntent)
        KeepAndFlipImgeActivity.thisActivity!!.finish()
        ScannerizationActivity.thisActivity!!.finish()
        finish()
    }
}