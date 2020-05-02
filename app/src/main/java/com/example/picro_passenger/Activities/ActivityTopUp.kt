package com.example.picro_passenger.Activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.R
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.CloudFunctions.QrCode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.WriterException

/**
 * ActivityTopUp
 *
 * Class untuk melakukan handle Top Up
 *
 * @author zredhard@gmail.com
 */
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
    }

    private fun elementInit(){

        /** inisialisasi elemen **/
        store      = FirebaseFirestore.getInstance()
        backButton = findViewById(R.id.backButton)
        qrCode     = findViewById<ImageView>(R.id.qrCode)
        tokenLabel = findViewById<TextView>(R.id.token_label)

        /** melakukan "generate" token pada awal **/
        getTopUpToken()

        /** tombol kembali **/
        backButton.setOnClickListener {
            finish()
        }

    }

    /**
     * getTopUpToken()
     * Fungsi untuk mengambil token dari database
     */
    private fun getTopUpToken(){
        val tokenData = store.document("picro_users/" + CloudFunctions.GetUserId())
        tokenData.addSnapshotListener { documentSnapshot, e ->
            if(documentSnapshot!!.exists()){
                val paymentToken = documentSnapshot.get("payment_token").toString()
                tokenLabel.text = CloudFunctions.TokenRegex(paymentToken)
                qrCode.setImageBitmap(QrCode.generate(paymentToken))
            }
        }
    }

}