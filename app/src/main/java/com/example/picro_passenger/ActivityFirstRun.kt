package com.example.picro_passenger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ActivityFirstRun : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        supportActionBar!!.hide()

        // go to main menu
        if(validateUserCredential()){
            val intent_to = Intent(this,ActivityMain::class.java)
            finish()
            startActivity(intent_to)
        }

        // go to splash page
        else{
            val intent_to = Intent(this,ActivitySplash::class.java)
            finish()
            startActivity(intent_to)
        }

    }

    // validate user credential
    fun validateUserCredential(): Boolean {
        if(CloudFunctions.ValidateUserSignInToken() == null){
            return false
        }
        return true
    }

}