package com.example.picro_passenger.cloud_functions

import android.content.Context
import com.example.picro_passenger.Dialog.DialogLoading
import com.google.firebase.functions.FirebaseFunctions

class PaymentHandler {

    private var functions : FirebaseFunctions? = null

    fun FirebaseFunctionInstance() : FirebaseFunctions?{
        functions = FirebaseFunctions.getInstance()
        return functions
    }

    fun MakePayment(context : Context, senderId:String, receiverId:String, amount:Int){

        val loading : DialogLoading = DialogLoading()
        //val state : DialogState = DialogState()

        // change to object
        val data = hashMapOf("senderId" to senderId,
                                                   "receiverId" to receiverId,
                                                   "amount" to amount.toString())

        // show loading instance
        loading.show(context)

        FirebaseFunctionInstance()
            ?.getHttpsCallable("PaymentCloud")
            ?.call(data)
            ?.addOnFailureListener {

            }
            ?.addOnSuccessListener {

            }
    }

    interface PaymentStatus {
        fun PaymentHandlerStatus(StatusCode : String, StatusMsg : String)
    }

}