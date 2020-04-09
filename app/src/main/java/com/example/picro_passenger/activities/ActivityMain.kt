package com.example.picro_passenger.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions

class ActivityMain : AppCompatActivity(){

    private lateinit var signOutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        /*
        signOutButton = findViewById(R.id.SignOut)

        if(CloudFunctions.ValidateUserSignInToken()){

        }

        Log.i("FirebaseSignIn", "Verify token")
        Log.i("FirebaseSignIn", CloudFunctions.ValidateUserSignInToken().toString())

        signOutButton.setOnClickListener {
            CloudFunctions.SignOut()
            val intent_to = Intent(this, ActivitySplash::class.java)
            finish()
            startActivity(intent_to)
        }*/

    }
}