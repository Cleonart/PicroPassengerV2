package com.example.picro_passenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.Activities.ActivityMain
import com.example.picro_passenger.ActivityDriver.DriverMainActivity
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.preuse_activities.ActivitySignIn
import com.example.picro_passenger.Support.SupportVerifyCard
import com.example.picro_passenger.newSupport.IntentControl
import com.example.picro_passenger.newSupport.SharedPreferencesService

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

        IntentControl.NavigatingTo(this, splashSignIn, ActivitySignIn::class.java)
        IntentControl.NavigatingTo(this, splashSignUp, SupportVerifyCard::class.java)
        IntentControl.NavigatingTo(this, splashRegisterNewCard, ActivitySignIn::class.java)
    }

    override fun onResume() {
        super.onResume()
        validateUser()
    }

    // validate if the user is already signed in
    private fun validateUser(){
        if(CloudFunctions.ValidateUserSignInToken()){
            val userType = SharedPreferencesService.PreferencesGet(baseContext, "userType")
            if(userType == "passenger" || userType == "owner"){
                IntentControl.IntentNavigation(this, ActivityMain::class.java, true)
            }
            else if(userType == "driver"){
                IntentControl.IntentNavigation(this, DriverMainActivity::class.java, true)
            }
        }
    }
}
