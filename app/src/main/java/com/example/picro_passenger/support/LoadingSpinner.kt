package com.example.picro_passenger.support

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.R
import kotlinx.android.synthetic.main.activity_splash.view.*

class LoadingSpinner : AppCompatActivity(){

    lateinit var loadingShow : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingShow = findViewById(R.id.loading_spinner)
    }

    fun showLoader(){
        loadingShow.visibility = View.VISIBLE
    }

    fun hideLoader(){
        loadingShow.visibility = View.INVISIBLE
    }

}