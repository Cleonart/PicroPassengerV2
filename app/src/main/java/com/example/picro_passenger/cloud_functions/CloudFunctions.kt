package com.example.picro_passenger.cloud_functions

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.*

object CloudFunctions{

    private var auth: FirebaseAuth? = null

    fun FirebaseAuthInstance() : FirebaseAuth?{
        auth = FirebaseAuth.getInstance()
        return auth
    }

    // sign out method
    fun SignOut(){
        FirebaseAuthInstance()?.signOut()
    }

    // validate token
    fun ValidateUserSignInToken() : Boolean{
        val currentUser = FirebaseAuthInstance()?.currentUser ?: return false
        return true
    }

    fun GetUserId() : String{
        val currentUser = FirebaseAuthInstance()?.currentUser
        val userId = currentUser!!.uid
        Log.d("USER", userId)
        return userId
    }

    // currency formatting
    fun Currency(balance:Double) : String{
        val localeId = Locale("in", "ID")
        val toIdCurrency : NumberFormat = NumberFormat.getCurrencyInstance(localeId)
        return toIdCurrency.format(balance).toString()
    }

    // 10 digit token regex
    fun TokenRegex(token:String):String{
        return token.replace(Regex("(\\d{3})(\\d{3})(\\d{2})(\\d{2})"), "$1 $2 $3 $4")
    }
}