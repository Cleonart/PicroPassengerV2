package com.example.picro_passenger.Activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picro_passenger.ActivitySplash
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.Dialog.BottomSheetPassengerQuantity
import com.example.picro_passenger.Model.UserActivityItem
import com.example.picro_passenger.R
import com.example.picro_passenger.payment_controller.ActivityScanner
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ActivityMain : AppCompatActivity() {

    lateinit var store : FirebaseFirestore
    lateinit var adapters : FirestoreRecyclerAdapter<UserActivityItem, UserAcitivtyHolder>
    lateinit var bottomSheet : BottomSheetPassengerQuantity

    private lateinit var mainUserBalanceLabel : TextView
    private lateinit var toUserAccount : LinearLayout
    private lateinit var toTopUp : ConstraintLayout
    private lateinit var toTransfer : ConstraintLayout
    private lateinit var buttonPay : LinearLayout
    private lateinit var rvUserActivity : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_main)
        supportActionBar!!.hide()

        store = FirebaseFirestore.getInstance()
        mainUserBalanceLabel = findViewById(R.id.main_user_balance_label)

        // initialize payment button
        initPayButton()
        toUserAccount = findViewById(R.id.activity_main_bottom_navigation_user)
        toTopUp       = findViewById(R.id.top_up_button)
        toTransfer    = findViewById(R.id.transfer_button)

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

        adapters = object : FirestoreRecyclerAdapter<UserActivityItem, UserAcitivtyHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAcitivtyHolder {
                val view : View = LayoutInflater.from(parent.context).inflate(R.layout._general_recent_transaction_item, parent, false)
                return UserAcitivtyHolder(view)
            }

            override fun onBindViewHolder(holder: UserAcitivtyHolder, position: Int, model: UserActivityItem) {

                var amountTotal = CloudFunctions.Currency(model.invoice_amount!!.toDouble())

                /** Payment **/
                if(model.invoice_type == "100"){
                    holder.invoice_id.text = "Bayar Mikrolet"
                    holder.invoice_amount.setTextColor(resources.getColor(R.color.danger))
                    holder.invoice_image.setImageResource(R.drawable.scan_icon)
                    amountTotal = "-" + amountTotal
                }

                /** Top up **/
                else if(model.invoice_type == "200"){
                    holder.invoice_id.text = "Top Up Saldo"
                    holder.invoice_amount.setTextColor(resources.getColor(R.color.success))
                    holder.invoice_image.setImageResource(R.drawable.top_up_icon)
                    amountTotal = "+" + amountTotal
                }

                holder.invoice_amount.text = amountTotal
                holder.invoice_date.text = model.invoice_datetime
            }

        }

        adapters.startListening()
        rvUserActivity.adapter = adapters
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

    override fun onStop() {
        super.onStop()
        adapters.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapters.startListening()
    }

}

class UserAcitivtyHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal lateinit var invoice_id: TextView
    internal lateinit var invoice_amount: TextView
    internal lateinit var invoice_date: TextView
    internal lateinit var invoice_image : ImageView

    init{
        invoice_image = itemView.findViewById(R.id.user_activity_image)
        invoice_id = itemView.findViewById(R.id.user_activity_name)
        invoice_amount = itemView.findViewById(R.id.user_activity_amount)
        invoice_date = itemView.findViewById(R.id.user_activity_date)
    }
}

