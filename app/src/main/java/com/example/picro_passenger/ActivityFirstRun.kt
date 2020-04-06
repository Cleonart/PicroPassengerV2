package com.example.picro_passenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.cloud_functions.CloudFunctions
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