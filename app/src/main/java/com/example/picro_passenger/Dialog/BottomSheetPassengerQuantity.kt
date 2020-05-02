package com.example.picro_passenger.Dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.picro_passenger.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class BottomSheetPassengerQuantity {

    // class init
    private var dialog : Dialog? = null
    private var passengerQuantity : PassengerQuantity? = null

    // element item
    private lateinit var BottomSheet_PayForFour : LinearLayout
    private lateinit var BottomSheet_PayForThree : LinearLayout
    private lateinit var BottomSheet_PayForTwo : LinearLayout
    private lateinit var BottomSheet_PayForOne : LinearLayout

    // interface setter
    fun setPaymentQuantity(passengerQuantity: PassengerQuantity){
        this.passengerQuantity = passengerQuantity
    }

    fun show(context: Context) : Dialog?{
        return show(context, true, null)
    }

    // show the bottom sheet modal
    fun show(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): Dialog?{

        // inflator and view settings
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflator.inflate(R.layout.bottomsheet_passenger_quantity, null)

        // element init
        elementInit(view)

        // dialog constructor
        dialog = BottomSheetDialog(context)
        dialog!!.setContentView(view)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        dialog!!.show()
        return dialog
    }

    fun elementInit(view : View){

        // element
        BottomSheet_PayForOne   = view.findViewById(R.id.BottomSheet_PayForOne)
        BottomSheet_PayForTwo   = view.findViewById(R.id.BottomSheet_PayForTwo)
        BottomSheet_PayForThree = view.findViewById(R.id.BottomSheet_PayForThree)
        BottomSheet_PayForFour  = view.findViewById(R.id.BottomSheet_PayForFour)

        // pay for one
        BottomSheet_PayForOne.setOnClickListener {
            passengerQuantity?.countQuantity(1, "Bayar sendiri")
            dialog!!.dismiss()
        }

        // pay for two
        BottomSheet_PayForTwo.setOnClickListener {
            passengerQuantity?.countQuantity(2, "Bayar berdua")
            dialog!!.dismiss()
        }

        // pay for three
        BottomSheet_PayForThree.setOnClickListener {
            passengerQuantity?.countQuantity(3, "Bayar bertiga")
            dialog!!.dismiss()
        }

        // pay for four
        BottomSheet_PayForFour.setOnClickListener {
            passengerQuantity?.countQuantity(4, "Bayar berempat")
            dialog!!.dismiss()
        }

    }

    fun getDialog() : Dialog?{
        return dialog
    }

    // inteface for bottom sheet
    interface PassengerQuantity{
        fun countQuantity(qty : Int, msg : String)
    }

}