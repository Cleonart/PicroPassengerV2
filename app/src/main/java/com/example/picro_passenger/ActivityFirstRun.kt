package com.example.picro_passenger

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.cloud_functions.CloudFunctions

class ActivityFirstRun : AppCompatActivity() {

    lateinit var intentControl : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_run)
        supportActionBar!!.hide()
        val r = Runnable {

            // pergi ke menu utama
            if(CloudFunctions.ValidateUserSignInToken()){
                intentControl = Intent(this,ActivityMain::class.java)
            }

            // pergi ke menu splash
            else{
                intentControl = Intent(this,ActivitySplash::class.java)
            }
            finish()
            startActivity(intentControl)
        }

        Handler().postDelayed(r, 400)

    }

}

