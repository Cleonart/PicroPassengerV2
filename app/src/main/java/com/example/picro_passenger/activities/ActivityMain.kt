package com.example.picro_passenger.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.payment_controller.ActivityScanner

class ActivityMain : AppCompatActivity(){

    private lateinit var toUserAccount : LinearLayout
    private lateinit var toTopUp : ConstraintLayout
    private lateinit var toTransfer : ConstraintLayout
    private lateinit var buttonPay : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        supportActionBar!!.hide()

        // initialize payment button
        initPayButton()
        toUserAccount = findViewById(R.id.activity_main_bottom_navigation_user)
        toTopUp       = findViewById(R.id.top_up_button)
        toTransfer    = findViewById(R.id.transfer_button)

        // validate user account
        if(!CloudFunctions.ValidateUserSignInToken()){
            val intent_to = Intent(this, ActivitySplash::class.java)
            finish()
            startActivity(intent_to)
        }

        // navigation
        navigationOnMain()

    }

    // initialize pay button
    private fun initPayButton(){
        buttonPay = findViewById(R.id.activity_main_button_pay)
        buttonPay.setOnClickListener {
            val intent_to = Intent(this, ActivityScanner::class.java)
            startActivity(intent_to)
        }
    }

    // navigation
    private fun navigationOnMain(){

        // navigating to user account
        toUserAccount.setOnClickListener {
            val intent_to = Intent(this, ActivityUserAccount::class.java)
            startActivity(intent_to)
        }

        // navigating to top up page
        toTopUp.setOnClickListener {
            val intent_to = Intent(this, ActivityTopUp::class.java)
            startActivity(intent_to)
        }

        // navigating to transfer page
        toTransfer.setOnClickListener {
            val intent_to = Intent(this, ActivityTopUp::class.java)
            startActivity(intent_to)
        }


    }
}