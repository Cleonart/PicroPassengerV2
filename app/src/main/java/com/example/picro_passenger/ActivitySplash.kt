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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        val splashSignIn = findViewById<Button>(R.id.splashSignIn)
        val splashSignUp = findViewById<Button>(R.id.splashSignUp)
        val splashRegisterNewCard = findViewById<Button>(R.id.splashRegisterNewCardButton)

        CloudFunctions.ValidateUserTypeActivity(baseContext, this)

        IntentControl.NavigatingTo(this, splashSignIn, ActivitySignIn::class.java)
        IntentControl.NavigatingTo(this, splashSignUp, SupportVerifyCard::class.java)
        IntentControl.NavigatingTo(this, splashRegisterNewCard, ActivitySignIn::class.java)
    }

    override fun onResume() {
        super.onResume()
        CloudFunctions.ValidateUserTypeActivity(baseContext, this)
    }

}
