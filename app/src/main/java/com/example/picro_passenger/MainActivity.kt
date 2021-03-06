package com.example.picro_passenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.picro_passenger.CloudFunctions.CloudFunctions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        Log.i("FirebaseSignIn", "Verify token")
        Log.i("FirebaseSignIn", CloudFunctions.ValidateUserSignInToken().toString())
    }
}
