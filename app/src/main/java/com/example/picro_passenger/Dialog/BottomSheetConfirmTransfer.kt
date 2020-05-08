package com.example.picro_passenger.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.example.picro_passenger.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetConfirmTransfer {

    private var dialog: Dialog? = null
    private var transferConfirmation: TransferConfirmation? = null

    // interface setter
    fun setTransferConfirmationInterface(transferConfirmation: TransferConfirmation){
        this.transferConfirmation = transferConfirmation
    }

    fun showTransferConfirmationDialog(context: Context, paymentToken: String, amount: Int): Dialog?{
        return showTransferConfirmationDialog(context, paymentToken, amount, true, null)
    }

    // show the bottom sheet modal
    fun showTransferConfirmationDialog(context: Context, paymentToken: String, amount: Int, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): Dialog?{

        // inflator and view settings
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflator.inflate(R.layout.bottomsheet_transfer_confirmation, null)

        // element init
        elementInit(view, paymentToken, amount)

        // dialog constructor
        dialog = BottomSheetDialog(context)
        dialog!!.setContentView(view)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        dialog!!.show()
        return dialog
    }

    fun elementInit(view: View, paymentToken: String, amount: Int){

    }

    private fun getUserDetail(){

    }

    interface TransferConfirmation{
        fun transferConfirmation(confirmed: Boolean)
    }
}