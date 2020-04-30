package com.example.picro_passenger.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater

class DialogLoading {

    private var dialog: Dialog? = null

    fun show(context: Context): Dialog? {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog? {
        return show(context, title, false, null)
    }

    fun show(context: Context, title: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): Dialog? {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(com.example.picro_passenger.R.layout._general_loading_anim, null)

        dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setContentView(view)
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        dialog!!.show()
        return dialog
    }

    fun hide(){
        dialog!!.dismiss()
    }

    fun getDialog(): Dialog? {
        return dialog
    }
}