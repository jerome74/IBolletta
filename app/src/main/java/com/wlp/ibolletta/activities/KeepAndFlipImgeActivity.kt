package com.wlp.ibolletta.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import com.theartofdev.edmodo.cropper.CropImageView
import com.wlp.ibolletta.R
import com.wlp.ibolletta.util.EXTRA_FILE
import kotlinx.android.synthetic.main.activity_keep_and_flip_imge.*

class KeepAndFlipImgeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keep_and_flip_imge)

        val uriImg = intent.getStringExtra(EXTRA_FILE)
        setSupportActionBar(toolbar_keep);
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        cropImageView.setImageUriAsync(Uri.parse(uriImg));


    }

    fun onRotateImgClicked(view: android.view.View) {

        cropImageView.rotateImage(-90)
    }

    fun onRotateScreenClicked(view: android.view.View) {

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            cropImageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            cropImageView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    companion object {
        var croppedImage : Bitmap? = null
        var thisActivity : Activity? = null
    }

    fun onNextStepClicked(view : View){

        cropImageView.setOnCropImageCompleteListener(CropImageView.OnCropImageCompleteListener { view, result ->
            croppedImage = result.bitmap
            thisActivity = this@KeepAndFlipImgeActivity
            val localIntent = Intent(this@KeepAndFlipImgeActivity, ScannerizationActivity::class.java)
            startActivity(localIntent);
            })

        cropImageView.getCroppedImageAsync();

    }
}