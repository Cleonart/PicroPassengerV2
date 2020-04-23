package com.example.picro_passenger.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.R

class ActivityTopUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_top_up)
        supportActionBar!!.hide()
    }

}