package com.example.picro_passenger.payment_controller

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.Dialog.BottomSheetPassengerQuantity
import com.example.picro_passenger.Dialog.DialogState
import com.example.picro_passenger.R
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.CloudFunctions.PaymentHandler
import com.google.firebase.functions.FirebaseFunctions
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ActivityScanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var functions: FirebaseFunctions
    lateinit var backButton : RelativeLayout

    lateinit var Scanner_PassengerQuantity_Container : LinearLayout
    lateinit var Scanner_PassengerQuantity_TextView : TextView

    private var mScannerView: ZXingScannerView? = null
    private var state : DialogState? = null
    private var amount : Int = 1
    private var bottomSheet : BottomSheetPassengerQuantity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._general_activity_scanner)
        supportActionBar!!.hide()

        // initialize all element
        elementInit()

        // permission handler
        permissionHandler()
    }

    private fun elementInit(){

        // back button
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // text view
        Scanner_PassengerQuantity_TextView = findViewById(R.id.Scanner_PassengerQuantity_TextView)
        Scanner_PassengerQuantity_TextView.text = "Bayar sendiri"

        bottomSheet = BottomSheetPassengerQuantity()
        bottomSheet?.setPaymentQuantity(object : BottomSheetPassengerQuantity.PassengerQuantity{
            override fun countQuantity(qty: Int, msg: String) {
                Scanner_PassengerQuantity_TextView.text = msg
                amount = qty
            }
        })

        Scanner_PassengerQuantity_Container = findViewById(R.id.Scanner_PassengerQuantity_Container)
        Scanner_PassengerQuantity_Container.setOnClickListener {
            bottomSheet?.show(this@ActivityScanner)
        }

        // initialize barcode scanner component Zxing and dialog state class
        mScannerView = findViewById<ZXingScannerView>(R.id.rxscan)
        state = DialogState()
    }

    private fun permissionHandler(){
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mScannerView!!.setResultHandler(this@ActivityScanner)
                    mScannerView!!.startCamera()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(applicationContext, "Akses kamera kamu harus diaktifkan", Toast.LENGTH_LONG).show()
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {}
            }).check()
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        val senderId= CloudFunctions.GetUserId()
        val receiverId = rawResult.text
        pay(senderId, receiverId)
    }

    private fun pay(senderId:String, receiverId:String){
        val paymentHandler : PaymentHandler = PaymentHandler()
        paymentHandler.setPaymentStatus(object : PaymentHandler.PaymentStatus{
            override fun PaymentHandlerStatus(StatusCode: String, StatusMsg: String) {

                when (StatusCode) {
                    "CONNECTION_PROBLEM" -> {
                        state!!.showFailed(this@ActivityScanner, "Koneksi Bermasalah")
                    }
                    "RECEIVER_NOT_FOUND" -> {
                        state!!.showFailed(this@ActivityScanner, "Kode QR tidak valid")
                    }
                    "SENDER_BALANCE_NOT_ENOUGH_FOR_TRANSACTION" -> {
                        val errorText = "Saldo anda tidak cukup\n" + StatusMsg
                        state!!.showFailed(this@ActivityScanner, errorText)
                    }
                    "PAYMENT SUCCESSFULL" -> {
                        state!!.showSuccess(this@ActivityScanner, "Pembayaran Berhasil")
                    }
                }

                Handler().postDelayed({
                    finish()
                },3200)
            }
        })
        paymentHandler.MakePayment(this, senderId, receiverId, amount)
    }


}