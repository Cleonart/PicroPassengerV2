package com.example.picro_passenger

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class ActivityFirstRun : AppCompatActivity() {

    lateinit var intentControl : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_run)
        supportActionBar!!.hide()

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.INTERNET)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    validate()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(applicationContext, "Akses kamera kamu harus diaktifkan", Toast.LENGTH_LONG).show()
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {}
            }).check()



    }

    fun validate(){
        val r = Runnable {

            // pergi ke menu utama
            intentControl = when {
                CloudFunctions.ValidateUserSignInToken() -> {
                    Intent(this,ActivityMain::class.java)
                }
                // pergi ke menu splash
                else -> {
                    Intent(this,ActivitySplash::class.java)
                }
            }
            finish()
            startActivity(intentControl)
        }

        Handler().postDelayed(r, 400)
    }

}

