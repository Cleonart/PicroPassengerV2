package com.example.picro_passenger.Adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.Model.UserActivityItem
import com.example.picro_passenger.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdapterUserActivity internal constructor (options: FirestoreRecyclerOptions<UserActivityItem>, userType: String) : FirestoreRecyclerAdapter<UserActivityItem, UserAcitivtyHolder>(options) {

    private var userType : String? = null

    init{
        this.userType = userType
    }

    /** on create view holder **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAcitivtyHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout._general_recent_transaction_item, parent, false)
        return UserAcitivtyHolder(view)
    }

    /** bind to view holder **/
    override fun onBindViewHolder(holder: UserAcitivtyHolder, position: Int, model: UserActivityItem) {

        val amountTotal = CloudFunctions.Currency(model.invoice_amount!!.toDouble())

        /** Payment **/
        if(model.invoice_type == "100"){
            if(userType == "passenger"){
                setItemAttribute(holder, "Bayar Mikrolet", "DANGER", amountTotal, R.drawable.scan_icon, model.invoice_date.toString())
            }
            else if(userType == "driver"){
                setItemAttribute(holder, "Terima Pempayaran", "SUCCESS", amountTotal, R.drawable.scan_icon, model.invoice_date.toString())
            }
        }

        /** Top up **/
        else if(model.invoice_type == "200") {
            if(userType == "passenger"){
                setItemAttribute(holder, "Topup Saldo", "SUCCESS", amountTotal, R.drawable.top_up_icon, model.invoice_date.toString())
            }
        }

    }

    /**
     * setItemAttribute
     * @param holder
     * @param itemText
     * @param itemColor
     * @param itemAmount
     * @param itemIcon
     * @param itemDate
     */
    fun setItemAttribute(holder: UserAcitivtyHolder, itemText: String, itemColor: String, itemAmount: String, itemIcon: Int, itemDate: String){

        var itemAmount_ = itemAmount

        holder.invoice_id.text = itemText
        holder.invoice_image.setImageResource(itemIcon)

        if(itemColor == "DANGER"){
            holder.invoice_amount.setTextColor(Color.rgb(46, 204, 113))
            itemAmount_ = "-$itemAmount"
        }
        else if(itemColor == "SUCCESS"){
            holder.invoice_amount.setTextColor(Color.rgb(46, 204, 113))
            itemAmount_ = "+$itemAmount"
        }

        holder.invoice_amount.text = itemAmount_
        holder.invoice_date.text = itemDate
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