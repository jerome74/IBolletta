package com.wlp.ibolletta.activities

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.googlecode.leptonica.android.GrayQuant
import com.googlecode.leptonica.android.Pix
import com.wlp.ibolletta.R
import com.wlp.ibolletta.domain.AuthObj
import com.wlp.ibolletta.util.OtsuThresholder
import kotlinx.android.synthetic.main.activity_scannerization.*
import java.nio.ByteBuffer

class ScannerizationActivity : AppCompatActivity() {

     var pix : Pix? = null

    companion object {
        var bitScannerization : Bitmap? = null
        var thisActivity : Activity? = null

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scannerization)

        setSupportActionBar(toolbar_scan);
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        pix = com.googlecode.leptonica.android.ReadFile.readBitmap(KeepAndFlipImgeActivity.croppedImage!!);
        val depth = pix!!.getDepth()
        val otsuThresholder = OtsuThresholder()
        var threshold = otsuThresholder.doThreshold(pix!!.getData());

        /* increase threshold because is better*/
        //threshold += 5;
        bitScannerization = com.googlecode.leptonica.android.WriteFile.writeBitmap(GrayQuant.pixThresholdToBinary(pix,threshold));

        try {
            croppedImage_img.setImageBitmap(bitScannerization);
        }catch (e : Exception){
            Log.e("Error", e.message , e)
        }


        opacity_sb.setProgress(Integer.valueOf((50 * threshold)/254));
        opacity_sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i("onProgressChanged","Not yet implemented")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {


                var thresh = Integer.valueOf(((256 * seekBar!!.getProgress())/100))

                if(thresh > 256) thresh = 256

                bitScannerization.takeIf { it != null }.apply { null }

                bitScannerization = com.googlecode.leptonica.android.WriteFile
                    .writeBitmap(GrayQuant.pixThresholdToBinary(pix,thresh!!))

                croppedImage_img.setImageBitmap(bitScannerization);
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.i("onStopTrackingTouch","Not yet implemented")
            }

        })


    }

    fun onOcrClicked(view : View){

        thisActivity = this@ScannerizationActivity

        startActivity(Intent(this@ScannerizationActivity, OCRActivity::class.java));
    }

    fun bitmapToArray(bitmap: Bitmap): ByteArray {

        val size = bitmap.getRowBytes() * bitmap.getHeight();

        var b = ByteBuffer.allocate(size);

        bitmap.copyPixelsToBuffer(b);

        val bytes = ByteArray(size)

        try {
            b.get(bytes, 0, bytes.size);
        } catch (e : Exception) {}
        return bytes
    }

    fun reduceQuality(bitmap: Bitmap, maxSize : Int) : Bitmap
    {
        var width = bitmap.getWidth();
        var height = bitmap.getHeight();

        val bitmapRatio = width.toFloat() / height.toFloat()

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (width / bitmapRatio).toInt();
        } else {
            height = maxSize;
            width = (height / bitmapRatio).toInt();
        }


        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    fun toGrayscale(bmpOriginal : Bitmap) : Bitmap
    {
        val height = bmpOriginal.getHeight();
        val width = bmpOriginal.getWidth();

        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0.toFloat());
        val f = ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.toFloat(), 0.toFloat(), paint);
        return bmpGrayscale;
    }
}