package com.example.picro_passenger.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.example.picro_passenger.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class BottomSheetPassengerSize() {

    private var dialog: Dialog? = null
    private var typeChoose : TypeChoose? = null

    // interface setter
    fun setPaymentHandler(typeChoose: TypeChoose) {
        this.typeChoose = typeChoose
    }

    fun show(context: Context): Dialog? {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog? {
        return show(context, title, false)
    }

    fun show(context: Context, title: CharSequence?, cancelable: Boolean): Dialog? {
        return show(context, title, cancelable, null)
    }

    fun show(context: Context, title: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): Dialog? {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflator.inflate(R.layout._general_loading_anim, null)
        typeChoose?.passengerSizeClick("tes")
        dialog = BottomSheetDialog(context)
        dialog!!.setContentView(view)
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        dialog!!.show()
        return dialog
    }

    fun getDialog(): Dialog? {
        return dialog!!
    }

    interface TypeChoose{
        fun passengerSizeClick(data:String)
    }

}

