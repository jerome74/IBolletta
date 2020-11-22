package com.wlp.ibolletta.domain

import android.graphics.Bitmap
import com.wlp.ibolletta.util.ThisApplication
import com.wlp.ibolletta.util.ThreadProgressBar

object AuthObj
{
    var token
        get() = ThisApplication.shardPrefs.token()!!
        set(value) = ThisApplication.shardPrefs.token(value)

    var isLoggIn
        get() = ThisApplication.shardPrefs.isLoggIn()!!
        set(value) = ThisApplication.shardPrefs.isLoggIn(value)

    var email
        get() = ThisApplication.shardPrefs.email()!!
        set(value) = ThisApplication.shardPrefs.email(value)

    var notify
        get() = ThisApplication.shardPrefs.notify()!!
        set(value) = ThisApplication.shardPrefs.notify(value)

    var thread : ThreadProgressBar? = null
    var fileTypeDownloadVideo = ""

    var bitScannerization : Bitmap? = null


    fun reset()
    {
        token  = ""
        isLoggIn = false
        fileTypeDownloadVideo = ""
        thread = null
        bitScannerization = null
    }
}