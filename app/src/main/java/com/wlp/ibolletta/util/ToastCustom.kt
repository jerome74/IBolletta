package com.wlp.ibolletta.util

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wlp.ibolletta.R
import com.wlp.ibolletta.IBollettaActivity
import kotlinx.android.synthetic.main.toast_custom.view.*

object ToastCustom
{
    fun show(ibolletta : Activity, text : String) {

        val layoutInflater = ibolletta.layoutInflater
        //Creating the LayoutInflater instance
        val li: LayoutInflater = layoutInflater
        //Getting the View object as defined in the customtoast.xml file
        val layout: View = li.inflate(R.layout.toast_custom,ibolletta.findViewById<ViewGroup>(R.id.custom_toast_layout))
        //Creating the Toast object
        layout.tv_toast_msg.text = " $text "
        //Creating the Toast object
        val toast = Toast(ibolletta.applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.view = layout //setting the view of custom toast layout

        toast.show()

        try {Thread.sleep(500) } catch (e: InterruptedException) {}
    }
}