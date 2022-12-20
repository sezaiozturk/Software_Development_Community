package com.ktunsdc.software_development_community.util

import android.app.Activity
import android.app.AlertDialog
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class CustomAlertDialog() {
    private lateinit var isDialog:AlertDialog

    fun create(fragment: Fragment,layout:Int)
    {
        val dialogView=fragment.layoutInflater.inflate(layout,null)
        val alertDialog=AlertDialog.Builder(fragment.context)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)
        isDialog=alertDialog.create()
    }
    fun mainCreate(activity:Activity,layout:Int)
    {
        val dialogView=activity.layoutInflater.inflate(layout,null)
        val alertDialog=AlertDialog.Builder(activity)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)
        isDialog=alertDialog.create()
    }
    fun open(){
        isDialog.show()
        isDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
    fun close(){
        isDialog.dismiss()
    }
    fun button(button: Int): Button {
        return isDialog.findViewById(button)
    }
    fun imageButton(button: Int): ImageView {
        return isDialog.findViewById(button)
    }
}