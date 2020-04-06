package com.example.picro_passenger.cloud_functions

import android.content.Intent
import android.util.Log
import com.example.picro_passenger.activities.ActivityMain
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult

object CloudFunctions{

    lateinit var auth: FirebaseAuth

    fun SignUp() {

    }

    // sign out method
    fun SignOut(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
    }

    // validate token
    fun ValidateUserSignInToken() : FirebaseUser?{
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        return currentUser
    }
}