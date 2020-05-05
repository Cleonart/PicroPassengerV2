package com.example.picro_passenger.CloudFunctions

import android.content.Context
import com.example.picro_passenger.Dialog.DialogLoading
import com.example.picro_passenger.Support.JsonObjectPaymentConfirmation
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson

/**
 * PaymentHandler()
 * Handler Pembayaran
 *
 * Class untuk melakukan handle pembayaran
 * Class juga dapat digunakan untuk memproses transfer
 *
 * How to use?
 * val paymentHandler : PaymentHandler = PaymentHandler()
 * paymentHandler("SENDER_UID", "RECEIVER_ID", "AMOUNT")
 * Jangan lupa untuk melakukan implementasi interface
 *
 * @author zredhard@gmail.com
 */
class PaymentHandler {

    private var functions : FirebaseFunctions? = null
    private var paymentStatus : PaymentStatus? = null

    private fun FirebaseFunctionInstance() : FirebaseFunctions?{
        functions = FirebaseFunctions.getInstance()
        return functions
    }

    /** setter untuk interface */
    fun setPaymentStatus(paymentStatus: PaymentStatus){
        this.paymentStatus = paymentStatus
    }

    fun MakePayment(context : Context, senderId:String, receiverId:String, amount:Int){

        /** Menampilkan dialog loading */
        val loading = DialogLoading()
        loading.show(context)

        /** Membuat object mapping sebagai parameter cloud function */
        val data = hashMapOf(
            "senderId" to senderId,
            "receiverId" to receiverId,
            "amount" to amount.toString())

        /** Memproses pembayaran **/
        FirebaseFunctionInstance()
            ?.getHttpsCallable("PaymentCloud")
            ?.call(data)
            ?.addOnFailureListener {
                loading.hide()
                paymentStatus?.PaymentHandlerStatus("CONNECTION_PROBLEM", "Periksa kembali koneksi internet anda")
            }
            ?.addOnSuccessListener {
                val resultData: String = Gson().toJson(it.data).toString()
                val response = Gson().fromJson(resultData, JsonObjectPaymentConfirmation::class.java)
                val errorCode: String? = response.error_code
                val errorMsg: String?  = response.error_msg
                loading.hide()
                paymentStatus?.PaymentHandlerStatus(errorCode.toString(), errorMsg.toString())
            }
    }

    interface PaymentStatus {
        fun PaymentHandlerStatus(StatusCode : String, StatusMsg : String)
    }

}