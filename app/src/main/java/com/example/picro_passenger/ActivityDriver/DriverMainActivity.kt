package com.example.picro_passenger.ActivityDriver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.Activities.ActivityUserAccount
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.CloudFunctions.QrCode
import com.example.picro_passenger.R
import com.example.picro_passenger.newSupport.IntentControl
import com.google.firebase.firestore.FirebaseFirestore

class DriverMainActivity : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    private var dailyRev : Float? = null
    private var targetAmount : Long? = null
    private var mikroOwnerId : String? = null

    private lateinit var DriverMain_status : TextView
    private lateinit var DriverMain_date : TextView
    private lateinit var DriverMain_qrCode : ImageView
    private lateinit var DriverTo_ListActivity : LinearLayout
    private lateinit var DriverTo_UserProfile : RelativeLayout

    private var oneTimeOnly : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driver_main_activity)
        supportActionBar!!.hide()
        elementInit()
        getUserDailyRevenue()
    }

    fun elementInit(){
        store = FirebaseFirestore.getInstance()
        DriverMain_qrCode = findViewById(R.id.DriverMain_qrCode)
        DriverMain_qrCode.setImageBitmap(QrCode.generate(CloudFunctions.GetUserId()))
        DriverMain_status = findViewById(R.id.DriverMain_status)
        DriverMain_date = findViewById(R.id.DriverMain_date)

        /** [NAVIGATION] Pergi ke halaman pengaturan pengguna */
        //DriverTo_UserProfile = findViewById(R.id.DriverTo_UserAccount)
        //IntentControl.NavigatingTo(this, DriverTo_UserProfile, ActivityUserAccount::class.java)

        /** [NAVIGATION] Pergi ke halaman daftar aktivitas*/
        DriverTo_ListActivity = findViewById(R.id.DriverTo_ListActivity)
        IntentControl.NavigatingTo(this, DriverTo_ListActivity, DriverListActivity::class.java)
    }

    /** Mengambil jumlah target pendapatan harian yang harus dipenuhi **/
    fun getTarget(){
        val GetTargetAmount = store.document("picro_profit_management/$mikroOwnerId/DRIVERS/${CloudFunctions.GetUserId()}")
        GetTargetAmount.get().addOnSuccessListener {
            targetAmount = it.get("profit_daily_target").toString().toLong()
            DriverMain_status.text = CloudFunctions.ChangePercentStatus("%.1f", dailyRev!!.toDouble(), targetAmount!!.toDouble())
        }
    }


    /** Mengambil jumlah pendapatan yang di dapat dalam sehari **/
    fun getUserDailyRevenue(){
        val userData = store.document("picro_users/" + CloudFunctions.GetUserId())
        userData.addSnapshotListener { snapshot, e ->
            if(snapshot!!.exists()){
                mikroOwnerId = snapshot.get("user_mikro_owner").toString()
                dailyRev = snapshot.get("user_daily_revenue").toString().toFloat()
                if(oneTimeOnly){
                    getTarget()
                    oneTimeOnly = false
                    return@addSnapshotListener
                }
                DriverMain_status.text = CloudFunctions.ChangePercentStatus("%.1f", dailyRev!!.toDouble(), targetAmount!!.toDouble())
            }
        }
    }
}