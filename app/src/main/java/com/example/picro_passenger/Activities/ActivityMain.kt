package com.example.picro_passenger.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.Adapter.AdapterUserActivity
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.Dialog.BottomSheetPassengerQuantity
import com.example.picro_passenger.Model.UserActivityItem
import com.example.picro_passenger.R
import com.example.picro_passenger.newSupport.IntentControl
import com.example.picro_passenger.payment_controller.ActivityScanner
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ActivityMain : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    lateinit var adapters : AdapterUserActivity
    lateinit var bottomSheet : BottomSheetPassengerQuantity

    private lateinit var mainUserBalanceLabel : TextView
    private lateinit var toUserAccount : LinearLayout
    private lateinit var toTopUp : ConstraintLayout
    private lateinit var toTransfer : ConstraintLayout
    private lateinit var toScanner : LinearLayout
    private lateinit var rvUserActivity : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        supportActionBar!!.hide()

        store = FirebaseFirestore.getInstance()
        mainUserBalanceLabel = findViewById(R.id.main_user_balance_label)

        // initialize payment button
        toUserAccount = findViewById(R.id.activity_main_bottom_navigation_user)
        toTopUp       = findViewById(R.id.top_up_button)
        toTransfer    = findViewById(R.id.transfer_button)
        toScanner     = findViewById(R.id.activity_main_button_pay)

        // initialize list item
        rvUserActivity = findViewById(R.id.rvUserActivity)

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

        // initialize list
        initializeList()
    }

    // initialize list item
    private fun initializeList(){

        rvUserActivity.layoutManager = LinearLayoutManager(this)

        /** Querying Data */
        val query : Query = store.collection("picro_log").whereArrayContains("invoice_user_involve",CloudFunctions.GetUserId()).orderBy("invoice_timestamp", Query.Direction.DESCENDING).limit(5)

        /** Recycler Options */
        val options : FirestoreRecyclerOptions<UserActivityItem> = FirestoreRecyclerOptions.Builder<UserActivityItem>()
                        .setQuery(query, UserActivityItem::class.java)
                        .build()

        adapters = AdapterUserActivity(options, "passenger")
        adapters.startListening()
        rvUserActivity.isNestedScrollingEnabled = false
        rvUserActivity.adapter = adapters
    }

    // navigation
    private fun navigationOnMain(){

        /** [NAVIGATION] Pergi ke halaman scanner */
        IntentControl.NavigatingTo(this, toScanner, ActivityScanner::class.java)

        /** [NAVIGATION] Pergi ke halaman detail pengguna */
        IntentControl.NavigatingTo(this, toUserAccount, ActivityUserAccount::class.java)

        /** [NAVIGATION] Pergi ke halaman top up */
        IntentControl.NavigatingTo(this, toTopUp, ActivityTopUp::class.java)

        /** [NAVIGATION] Pergi ke halaman transfer */
        IntentControl.NavigatingTo(this, toTopUp, ActivityTopUp::class.java)

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

    override fun onStop() {
        super.onStop()
        adapters.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapters.startListening()
    }
}



