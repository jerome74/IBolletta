package com.wlp.ibolletta.util

import android.widget.ProgressBar

class ThreadProgressBar(var prBar: ProgressBar) : Thread()
{

    var finish = false


    override fun run() {

            var i = 0
            prBar.progress = 0

            while (i < 100) {

                    if (finish)
                    {
                        prBar.max = i
                        i = 100
                    }

                    prBar.progress = i

                    try {Thread.sleep(1000) } catch (e: InterruptedException) {}

                    i++
            }
    }
}