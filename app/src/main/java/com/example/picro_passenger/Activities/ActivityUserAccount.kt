package com.example.picro_passenger.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.R
import com.example.picro_passenger.CloudFunctions.CloudFunctions

class ActivityUserAccount : AppCompatActivity(){

    lateinit var backButton : ImageView
    lateinit var signOutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_user_account)
        supportActionBar!!.hide()

        backButton = findViewById(R.id.back_button)
        signOutButton = findViewById(R.id.sign_out_button)

        // back to main activity
        backButton.setOnClickListener(View.OnClickListener {
            finish();
        })

        // sign out button
        signOutButton.setOnClickListener(View.OnClickListener {
            CloudFunctions.SignOut()
            val intentTo = Intent(this, ActivitySplash::class.java)
            finish()
            startActivity(intentTo)
        })

    }
}