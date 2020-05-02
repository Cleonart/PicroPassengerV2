package com.example.picro_passenger.Dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class DialogState {

    private var dialog: Dialog? = null
    private var DialogState_AnimationView : LottieAnimationView? = null
    private var DialogState_TextView : TextView? = null

    fun showSuccess(context: Context, msg : CharSequence) : Dialog?{
        return show(context, "SUCCESS", msg,false, null)
    }

    fun showFailed(context: Context, msg : CharSequence) : Dialog?{
        return show(context, "FAILED", msg,false, null)
    }

    private fun show(context: Context, typeState : CharSequence?, msg : CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): Dialog? {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(com.example.picro_passenger.R.layout._general_dialog_state, null)

        DialogState_AnimationView = view.findViewById<LottieAnimationView>(com.example.picro_passenger.R.id.DialogState_AnimationView)
        DialogState_TextView = view.findViewById<TextView>(com.example.picro_passenger.R.id.DialogState_TextView)

        if(typeState == "SUCCESS"){
            DialogState_AnimationView!!.setAnimation(com.example.picro_passenger.R.raw.success_2)
        }
        else if(typeState == "FAILED"){
            DialogState_AnimationView!!.setAnimation(com.example.picro_passenger.R.raw.failed)
        }

        DialogState_TextView!!.text = msg
        DialogState_AnimationView!!.playAnimation()

        dialog = Dialog(context, R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog!!.setContentView(view)
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        dialog!!.show()
        return dialog
    }

    fun dismiss(){
        dialog!!.dismiss()
    }

    fun getDialog(): Dialog? {
        return dialog
    }
}