package com.example.picro_passenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.preuse_activities.ActivitySignIn
import com.example.picro_passenger.support.LoadingSpinner
import com.example.picro_passenger.support.SupportVerifyCard
import com.google.firebase.functions.FirebaseFunctions

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

        // validate if the user is already signed in
        if(CloudFunctions.ValidateUserSignInToken()){
            intentControl = Intent(this, ActivityMain::class.java)
            finish()
            startActivity(intentControl)
        }

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
}
