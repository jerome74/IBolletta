package com.wlp.ibolletta.util

import android.os.AsyncTask
import android.os.HandlerThread

class TaskOCR(val handlerInBackground: () -> Unit, val handlerPostExecute: () -> Unit,
              name: String?
) : Thread() {

    override fun run() {
        super.run()
        handlerInBackground()
        handlerPostExecute()
    }
}