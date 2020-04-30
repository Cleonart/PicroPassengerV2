package com.example.picro_passenger.Dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.picro_passenger.R
import com.google.android.material.bottomsheet.BottomSheetDialog

object SuccessAndFailed{
    
    fun showSuccess(context : Context){
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout._general_loading_anim, null)
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(view)
        dialog.show()
    }

}