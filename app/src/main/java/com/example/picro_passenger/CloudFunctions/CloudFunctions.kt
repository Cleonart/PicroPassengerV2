package com.example.picro_passenger.CloudFunctions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.picro_passenger.Activities.ActivityMain
import com.example.picro_passenger.ActivityDriver.DriverMainActivity
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.newSupport.IntentControl
import com.example.picro_passenger.newSupport.SharedPreferencesService
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.*

object CloudFunctions{

    private var auth: FirebaseAuth? = null

    private fun FirebaseAuthInstance() : FirebaseAuth?{
        auth = FirebaseAuth.getInstance()
        return auth
    }

    /**
     * Mengeluarkan pengguna dari aplikasi
     */
    fun SignOut(){
        FirebaseAuthInstance()?.signOut()
    }

    /**
     * ValidateUserSignInToken()
     *
     * Melakukan validasi token
     * @return true : jika token ditemukan
     *         false : jika token tidak ditemukan
     */
    fun ValidateUserSignInToken() : Boolean{
        val currentUser = FirebaseAuthInstance()?.currentUser ?: return false
        return true
    }

     /**
     * GetUserId()
     *
     * Mengambil id dari user yang sedang login saat ini
     * @return String : userId
     */
    fun GetUserId() : String{
        val currentUser = FirebaseAuthInstance()?.currentUser
        val userId = currentUser!!.uid
        Log.d("USER", userId)
        return userId
    }

    /**
     * Currency(INT OR DOUBLE)
     *
     * Mengubah format ke dalam bentuk Rupiah
     * @param balance : Double - Jumlah uang yang tersisa
     * @return String : Ex. Rp20.000
     */
    fun Currency(balance: Double) : String{
        val localeId = Locale("in", "ID")
        val toIdCurrency : NumberFormat = NumberFormat.getCurrencyInstance(localeId)
        return toIdCurrency.format(balance).toString()
    }

    fun Percent(valueToConvert: Double, valueAtMax: Double): Float{
        var value = ((valueToConvert/valueAtMax) * 100).toFloat()
        if(value >= 100 || value >= 100F){
            value = 100F
        }
        return value
    }

    fun ChangePercentStatus(formatManual: String, valueToConvert: Double, valueAtMax: Double): String{
        val percent = formatManual.format(Percent(valueToConvert.toDouble(), valueAtMax.toDouble()))
        val user_daily_revenue = "$percent%"
        return user_daily_revenue
    }

    /**
     * TokenRegex(token:String)
     *
     * Fungsi untuk melakukan "generate" 10 digit token pembayaran
     * @param token : 10 digit string token
     * @return String : 10 digit token dengan format - XXX XXX XX XX
     */
    fun TokenRegex(token:String):String{
        return token.replace(Regex("(\\d{3})(\\d{3})(\\d{2})(\\d{2})"), "$1 $2 $3 $4")
    }

    /**
     * ValidateUserActivity
     *
     * fungsi untuk melakukan validasi activity dari pengguna
     * jika pengguna bertipe pengguna penumpang atau pemilik akan diarahkan ke ActivityMain
     * jika pengguna bertipe pengguna driver akan diarahkan ke DriverMainActivity
     *
     * @param BaseContextData : BaseContext dari aplikasi
     * @param intentActivity : Activity
     */
    fun ValidateUserTypeActivity(BaseContextData: Context, intentActivity: Activity, firstRun: Boolean = false){

        if(ValidateUserSignInToken()){

            val userType = SharedPreferencesService.PreferencesGet(BaseContextData, "userType")
            Log.d("userType", userType)
            if(userType == "passenger" || userType == "owner"){
                IntentControl.IntentNavigation(intentActivity, ActivityMain::class.java, true)
            }

            else if(userType == "driver"){
                IntentControl.IntentNavigation(intentActivity, DriverMainActivity::class.java, true)
            }

            else{
                if(firstRun){
                    IntentControl.IntentNavigation(intentActivity, ActivitySplash::class.java, true)
                }
            }

        }
        else{
            SharedPreferencesService.PreferencesSet(BaseContextData, "userType", "none")
            if(firstRun){
                IntentControl.IntentNavigation(intentActivity, ActivitySplash::class.java, true)
            }
        }

    }
}