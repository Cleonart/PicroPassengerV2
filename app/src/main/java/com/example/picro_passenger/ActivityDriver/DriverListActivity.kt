package com.example.picro_passenger.ActivityDriver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picro_passenger.Adapter.AdapterUserActivity
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.Model.UserActivityItem
import com.example.picro_passenger.R
import com.example.picro_passenger.Support.IntentControl
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DriverListActivity : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    private var DriverActivityList_rv : RecyclerView? = null
    lateinit var adapters : AdapterUserActivity
    lateinit var backButton : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.driver_activity_list)
        backButton = findViewById(R.id.backButton)
        IntentControl.FinishActivity(this, backButton)
        initializeList()
    }

    // initialize list item
    private fun initializeList(){

        store = FirebaseFirestore.getInstance()
        DriverActivityList_rv = findViewById(R.id.DriverActivityList_rv)
        DriverActivityList_rv!!.layoutManager = LinearLayoutManager(this)

        /** Querying Data */
        val query : Query = store.collection("picro_log").whereArrayContains("invoice_user_involve",
            CloudFunctions.GetUserId()).orderBy("invoice_timestamp", Query.Direction.DESCENDING).limit(5)

        /** Recycler Options */
        val options : FirestoreRecyclerOptions<UserActivityItem> = FirestoreRecyclerOptions.Builder<UserActivityItem>()
            .setQuery(query, UserActivityItem::class.java)
            .build()

        adapters = AdapterUserActivity(options, "driver")
        adapters.startListening()
        DriverActivityList_rv!!.isNestedScrollingEnabled = false
        DriverActivityList_rv!!.adapter = adapters
    }
}