package com.example.picro_passenger.activities

import android.R.attr.mode
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.Dialog.BottomSheetPassengerQuantity
import com.example.picro_passenger.Dialog.BottomSheetPassengerSize
import com.example.picro_passenger.Dialog.DialogLoading
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.payment_controller.ActivityScanner
import com.google.firebase.firestore.FirebaseFirestore


class ActivityMain : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    lateinit var bottomSheet : BottomSheetPassengerQuantity

    private lateinit var mainUserBalanceLabel : TextView
    private lateinit var toUserAccount : LinearLayout
    private lateinit var toTopUp : ConstraintLayout
    private lateinit var toTransfer : ConstraintLayout
    private lateinit var buttonPay : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        supportActionBar!!.hide()

        //val loading : DialogLoading = DialogLoading()
        //loading.show(this)

        //bottomSheet = BottomSheetPassengerQuantity()
        //bottomSheet.setPaymentQuantity(object : BottomSheetPassengerQuantity.PassengerQuantity{
        //    override fun countQuantity(qty: Int) {
        //        Log.i("Data", qty.toString())
        //    }
        //})
        //bottomSheet.show(this)

        store = FirebaseFirestore.getInstance()
        mainUserBalanceLabel = findViewById(R.id.main_user_balance_label)

        // initialize payment button
        initPayButton()
        toUserAccount = findViewById(R.id.activity_main_bottom_navigation_user)
        toTopUp       = findViewById(R.id.top_up_button)
        toTransfer    = findViewById(R.id.transfer_button)

        // validate user account
        if(!CloudFunctions.ValidateUserSignInToken()){
            val intent_to = Intent(this, ActivitySplash::class.java)
            finish()
            startActivity(intent_to)
        }

        // get realtime user balance
        updateUserData()

        // navigation
        navigationOnMain()

    }

    // initialize pay button
    private fun initPayButton(){
        buttonPay = findViewById(R.id.activity_main_button_pay)
        buttonPay.setOnClickListener {
            val intent_to = Intent(this, ActivityScanner::class.java)
            startActivity(intent_to)
        }
    }

    // navigation
    private fun navigationOnMain(){

        // navigating to user account
        toUserAccount.setOnClickListener {
            val intent_to = Intent(this, ActivityUserAccount::class.java)
            startActivity(intent_to)
        }

        // navigating to top up page
        toTopUp.setOnClickListener {
            val intent_to = Intent(this, ActivityTopUp::class.java)
            startActivity(intent_to)
        }

        // navigating to transfer page
        toTransfer.setOnClickListener {
            val intent_to = Intent(this, ActivityTopUp::class.java)
            startActivity(intent_to)
        }


    }

    private fun updateUserData(){
        val userData = store.document("picro_users/" + CloudFunctions.GetUserId())
        userData.addSnapshotListener { snapshot, e ->
            if(snapshot!!.exists()){
                val userbalance = snapshot.get("user_balance").toString()
                mainUserBalanceLabel.text = CloudFunctions.Currency(userbalance.toDouble())
            }
        }

    }

}