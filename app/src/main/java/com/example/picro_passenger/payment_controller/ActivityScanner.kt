package com.example.picro_passenger.payment_controller

import android.Manifest
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.R
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ActivityScanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    lateinit var backButton : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._general_activity_scanner)
        supportActionBar!!.hide()

        // back button
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        mScannerView = findViewById<ZXingScannerView>(R.id.rxscan)

        // permission handler
        permissionHandler()
    }

    private fun permissionHandler(){
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mScannerView!!.setResultHandler(this@ActivityScanner)
                    mScannerView!!.startCamera()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(applicationContext, "Akses kamera kamu harus diaktifkan", Toast.LENGTH_LONG).show()
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {}
            }).check()
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        Toast.makeText(applicationContext,rawResult.text ,Toast.LENGTH_LONG).show();
        //TODO implement the cloud functions for payment in this function
        mScannerView!!.resumeCameraPreview(this)
    }

}