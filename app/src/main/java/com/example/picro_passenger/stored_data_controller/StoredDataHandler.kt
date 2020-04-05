package com.example.picro_passenger.stored_data_controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences

class StoredDataHandler : AppCompatActivity() {

    // get stored data from shared preferences
    fun getStoredData(reference_name : String = "null"):String? {

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("PREF",Context.MODE_PRIVATE)

        //val pref = getSharedPreferences("PREF_ROOT",Context.MODE_PRIVATE)
        val dataToStore: String = reference_name

        return if(dataToStore != "null"){
            dataToStore
        }
        else{
            "null"
        }
    }

    // validate user credential
    fun validateUserCredential(): Boolean {
        return getStoredData("null") != "null"
    }


}
