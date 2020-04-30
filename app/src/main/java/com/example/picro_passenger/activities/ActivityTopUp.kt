package com.example.picro_passenger.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.Dialog.SuccessAndFailed
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.WriterException

class ActivityTopUp : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    lateinit var qrCode : ImageView
    lateinit var backButton : ConstraintLayout
    lateinit var tokenLabel : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_top_up)
        supportActionBar!!.hide()
        elementInit()
        SuccessAndFailed.showSuccess(baseContext)
    }

    private fun elementInit(){

        store      = FirebaseFirestore.getInstance()
        backButton = findViewById(R.id.backButton)
        qrCode     = findViewById<ImageView>(R.id.qrCode)
        tokenLabel = findViewById<TextView>(R.id.token_label)

        // generate token for top up
        getTopUpToken()

        // back button
        backButton.setOnClickListener {
            finish()
        }

    }

    // generate token qr code
    private fun generateTopUpQr(qrcode:String){

        // generate qr code
        val qrgEncoder = QRGEncoder(qrcode, null, QRGContents.Type.TEXT, 450)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.TRANSPARENT

        try { // Getting QR-Code as Bitmap
            val bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            qrCode.setImageBitmap(bitmap)
        } catch (e: WriterException) { }

    }

    // generate token number
    private fun getTopUpToken(){
        val tokenData = store.document("picro_users/" + CloudFunctions.GetUserId())
        tokenData.addSnapshotListener { documentSnapshot, e ->
            if(documentSnapshot!!.exists()){
                val paymentToken = documentSnapshot.get("payment_token").toString()
                tokenLabel.text = CloudFunctions.TokenRegex(paymentToken)
                generateTopUpQr(paymentToken)
            }
        }
    }

}