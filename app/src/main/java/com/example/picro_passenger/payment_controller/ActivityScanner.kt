package com.example.picro_passenger.payment_controller

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.support.JsonObjectPaymentConfirmation
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
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
    private var mScannerView: ZXingScannerView? = null
    lateinit var backButton : RelativeLayout
    lateinit var spinner : ConstraintLayout

    // animation
    lateinit var confirmation : ConstraintLayout
    private var animation_view_confirmation : LottieAnimationView? = null
    lateinit var lottie_animation_text : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._general_activity_scanner)
        supportActionBar!!.hide()

        confirmation = findViewById(R.id.confirmation)
        confirmation.visibility = View.GONE  // hide the confirmation
        animation_view_confirmation = findViewById<LottieAnimationView>(R.id.animation_view_confirmation)
        lottie_animation_text = findViewById(R.id.lottie_animation_text)

        spinner = findViewById(R.id.loading_spinner)
        spinner.visibility = View.GONE // hide the spinner

        // back button
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        mScannerView = findViewById<ZXingScannerView>(R.id.rxscan)

        // permission handler
        permissionHandler()
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
        spinner.visibility = View.VISIBLE
        val senderId = CloudFunctions.GetUserId()
        val receiverId = rawResult.text
        val amount = 4000
        pay(senderId, receiverId, 4000)
        //TODO implement the cloud functions for payment in this function

    }

    fun pay(senderId:String, receiverId:String, amount:Int){

        // dirubah menjadi object
        val data = hashMapOf("senderId" to senderId,
                                                    "receiverId" to receiverId)

        // inisialisasi fungsi firebase
        functions = FirebaseFunctions.getInstance()
        functions.getHttpsCallable("PaymentCloud")
                 .call(data)
                 .addOnFailureListener{
                     spinner.visibility = View.GONE // hide the spinner
                     Toast.makeText(baseContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                 }
                .addOnSuccessListener {
                    val resultData = Gson().toJson(it.data).toString()
                    val response = Gson().fromJson(resultData, JsonObjectPaymentConfirmation::class.java)
                    val errorCode = response.error_code
                    spinner.visibility = View.GONE            // hide the spinner
                    confirmation.visibility = View.VISIBLE    // show the confirmation component
                    Log.d("Payment", errorCode.toString())

                    when (errorCode) {
                        "RECEIVER_NOT_FOUND" -> {
                            lottie_animation_text.text = "Kode QR tidak valid"
                            animation_view_confirmation!!.setAnimation(R.raw.loading_spinner)
                        }
                        "SENDER_BALANCE_NOT_ENOUGH_FOR_TRANSACTION" -> {
                            lottie_animation_text.text = "Saldo anda tidak cukup"
                            //animation_view_confirmation.setAnimation(R.raw.failed)
                        }
                        "PAYMENT SUCCESSFULL" -> {
                            lottie_animation_text.text = "Pembayaran Berhasil"
                            //animation_view_confirmation.setAnimation(R.raw.success)
                        }
                    }

                    animation_view_confirmation!!.playAnimation()

                    // delay before going out
                    //Handler().postDelayed(
                     //   { Log.i("tag", "This'll run 1000 milliseconds later") },1200)

                }
    }

}