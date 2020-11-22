package com.wlp.ibolletta.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.KITKAT)
class TessOCR(val context: Context)
{

    lateinit var  mTess : TessBaseAPI;

    init {
        // TODO Auto-generated constructor stub
        mTess = TessBaseAPI()

        val datapath =  "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}/tesseract/"
        val language = "ita";
        val dir = File(datapath + "tessdata/")

        if (!dir.exists()) dir.mkdirs()

        val files = context.assets.list("trainneddata")

        files!!.forEach {

            val outFile = File(dir, it)

            if(!outFile.exists()){
                val inStream = context.assets.open("trainneddata/${it}")
                inStream.toFile(outFile)
            }
        }

        mTess.init(datapath, language);
    }


    fun InputStream.toFile(file: File) {
    use { input ->
        file.outputStream().use { input.copyTo(it) }
    }
}

    fun getOCRResult(bitmap : Bitmap) : String {

        mTess.setImage(bitmap);
        val result = mTess.getUTF8Text();

        return result;
    }

    fun onDestroy() = mTess.end()
}