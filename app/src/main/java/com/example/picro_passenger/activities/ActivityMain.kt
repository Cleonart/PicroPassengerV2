package com.example.picro_passenger.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.payment_controller.ActivityScanner

class ActivityMain : AppCompatActivity(){

    private lateinit var toUserAccount : LinearLayout
    private lateinit var buttonPay : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        supportActionBar!!.hide()

        initPayButton()
        toUserAccount = findViewById(R.id.activity_main_bottom_navigation_user)

        // validate user account
        if(!CloudFunctions.ValidateUserSignInToken()){
            val intent_to = Intent(this, ActivitySplash::class.java)
            finish()
            startActivity(intent_to)
        }

        // navigating to user account
        toUserAccount.setOnClickListener {
            val intent_to = Intent(this, ActivityUserAccount::class.java)
            startActivity(intent_to)
        }
    }

    // initialize pay button
    private fun initPayButton(){
        buttonPay = findViewById(R.id.activity_main_button_pay)
        buttonPay.setOnClickListener {
            val intent_to = Intent(this, ActivityScanner::class.java)
            startActivity(intent_to)
        }
    }
}