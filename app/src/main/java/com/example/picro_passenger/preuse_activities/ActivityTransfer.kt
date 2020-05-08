package com.example.picro_passenger.preuse_activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.CloudFunctions.CloudFunctions
import com.example.picro_passenger.Dialog.BottomSheetConfirmTransfer
import com.example.picro_passenger.Dialog.BottomSheetPassengerQuantity
import com.example.picro_passenger.R
import com.example.picro_passenger.newSupport.IntentControl
import com.example.picro_passenger.newSupport.SharedPreferencesService
import com.google.common.primitives.UnsignedBytes.toInt

class ActivityTransfer: AppCompatActivity() {
    
    private lateinit var Transfer_Continue: Button
    private lateinit var Transfer_TransferAmount: EditText
    private lateinit var Transfer_TransferToken: EditText
    private lateinit var Transfer_UserBalance: TextView

    private lateinit var backButton: ConstraintLayout
    private var bottomSheet : BottomSheetConfirmTransfer? = null

    private var user_balance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout._general_activity_transfer)
        elementInit()
    }

    private fun elementInit(){

        /** Edit text untuk token dan jumlah nominal transfer */
        Transfer_TransferAmount = findViewById(R.id.Transfer_TransferAmount)
        Transfer_TransferToken = findViewById(R.id.Transfer_TransferToken)
        Transfer_UserBalance = findViewById(R.id.Transfer_UserBalance)

        user_balance = SharedPreferencesService.PreferencesGet(baseContext, "user_balance")!!.toInt()
        Transfer_UserBalance.text = CloudFunctions.Currency(user_balance.toDouble())

        bottomSheet = BottomSheetConfirmTransfer()
        bottomSheet?.setTransferConfirmationInterface(object: BottomSheetConfirmTransfer.TransferConfirmation{
            override fun transferConfirmation(confirmed: Boolean) {

            }
        })

        Transfer_Continue = findViewById(R.id.Transfer_Continue)
        Transfer_Continue.setOnClickListener {
            if(validatingInput()){
                val TransferToken = Transfer_TransferToken.text.toString()
                val TransferAmount = Transfer_TransferAmount.text.toString()
                bottomSheet?.showTransferConfirmationDialog(this@ActivityTransfer, TransferToken, TransferAmount.toInt())
            }
        }

        backButton = findViewById(R.id.backButton)
        IntentControl.FinishActivity(this, backButton)
    }

    // passed test
    fun validatingInput(): Boolean {
        val TransferToken = Transfer_TransferToken.text.toString()
        val TransferAmount = Transfer_TransferAmount.text.toString()

        if(TransferToken == "") {
            Transfer_TransferToken.error = "Token harus diisi"
            Transfer_TransferToken.requestFocus()
        }

        if(TransferAmount != ""){
            Transfer_TransferAmount.error = "Jumlah transfer harus diisi"
        }

        // SUCCESS : jika username dan password telah memenuhi
        if(!(TransferAmount == "" && TransferToken == "")){

            val TransferAmount_int = TransferAmount.toInt()

            // validasi benar jika jumlah password adalah 6 digit
            if(TransferAmount_int >= 5000){
                return true
            }

            // validasi salah jika jumlah nominal top up lebih besar dari saldo pengguna
            else if(TransferAmount_int > user_balance){
                Transfer_TransferAmount.error = "Saldo anda tidak cukup"
                Transfer_TransferAmount.requestFocus()
            }

            // ERROR: kode otentikasi harus 6 digit
            Transfer_TransferAmount.error = "Minimal nominal transfer Rp.5000"
            Transfer_TransferAmount.requestFocus()
        }

        return false
    }


}