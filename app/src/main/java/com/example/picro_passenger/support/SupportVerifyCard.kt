package com.example.picro_passenger.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.R

class SupportVerifyCard : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_card)
        supportActionBar!!.hide()
    }

}