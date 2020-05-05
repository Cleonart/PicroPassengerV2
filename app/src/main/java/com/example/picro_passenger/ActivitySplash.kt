package com.example.picro_passenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.Activities.ActivityMain
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.preuse_activities.ActivitySignIn
import com.example.picro_passenger.Support.SupportVerifyCard

class ActivitySplash : AppCompatActivity(){

    lateinit var intentControl : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        intentControl = Intent()

        val splashSignIn = findViewById<Button>(R.id.splashSignIn)
        val splashSignUp = findViewById<Button>(R.id.splashSignUp)
        val splashRegisterNewCard = findViewById<Button>(R.id.splashRegisterNewCardButton)

        validateUser()

        splashSignIn.setOnClickListener {
            intentControl = Intent(this, ActivitySignIn::class.java)
            startActivity(intentControl)
        }

        splashSignUp.setOnClickListener {
            intentControl = Intent(this, SupportVerifyCard::class.java)
            startActivity(intentControl)
        }

        splashRegisterNewCard.setOnClickListener {
            intentControl = Intent(this, ActivitySignIn::class.java)
            startActivity(intentControl)
        }
    }

    override fun onResume() {
        super.onResume()
        validateUser()
    }

    // validate if the user is already signed in
    private fun validateUser(){
        if(CloudFunctions.ValidateUserSignInToken()){
            intentControl = Intent(this, ActivityMain::class.java)
            finish()
            startActivity(intentControl)
        }
    }
}
