package com.example.picro_passenger.newSupport

import android.app.Activity
import android.content.Intent
import android.view.View

object IntentControl{

    fun NavigatingTo(context: Activity, view: View, to: Class<*>, finish: Boolean= false){
        view.setOnClickListener {
            IntentNavigation(context, to, finish)
        }
    }

    fun IntentNavigation(context: Activity, to: Class<*>, finish: Boolean = false){
        val intentTo = Intent(context, to)
        if(finish){
            context.finish()
        }
        context.startActivity(intentTo)
    }

    fun FinishActivity(context: Activity, view: View){
        view.setOnClickListener {
            context.finish()
        }
    }

}