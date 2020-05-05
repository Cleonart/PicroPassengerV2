package com.example.picro_passenger.newSupport

import android.app.Activity
import android.content.Intent
import android.view.View

object IntentControl{

    fun NavigatingTo(context: Activity, view: View, to: Class<*>){
        view.setOnClickListener {
            val intentTo = Intent(context, to)
            context.startActivity(intentTo)
        }
    }

    fun FinishActivity(context: Activity, view: View){
        view.setOnClickListener {
            context.finish()
        }
    }

}