package com.example.picro_passenger.newSupport

import android.content.Context
import android.preference.PreferenceManager

object SharedPreferencesService {

    fun PreferencesSet(context: Context?, fieldName: String, fieldValue: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(fieldName, fieldValue)
        editor.apply()
    }

    fun PreferencesGet(context: Context, fieldName: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(fieldName, "GOT_NOTHING")
    }

    fun PreferencesAvailable(context: Context?, fieldName: String): Boolean{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefData = preferences.getString(fieldName, null)
        if(prefData == null){
            return false
        }
        return true
    }

}