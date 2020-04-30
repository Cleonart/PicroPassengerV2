package com.example.picro_passenger.Dialog

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class BottomSheetNoInternet {

    private var InternetConnection : ConnectivityManager? = null
    private var InternetStateInfo : NetworkInfo? = null

    fun checkConnectionState(context : Context){
        InternetConnection = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(InternetConnection == null){

        }
    }

}