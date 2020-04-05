package com.example.picro_passenger.preuse_activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.R
import com.example.picro_passenger.cloud_functions.CloudFunctions
import com.example.picro_passenger.support.JsonObjectSignInToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import org.json.JSONObject


class ActivitySignIn : AppCompatActivity(){

    lateinit var functions: FirebaseFunctions
    lateinit var auth: FirebaseAuth
    private var customToken: String? = null

    private val signInUsername : EditText = findViewById(R.id.sign_in_username)
    private val signInAuthCode : EditText = findViewById(R.id.sign_in_auth_code)
    private val showAuth: CheckBox = findViewById(R.id.show_auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar!!.hide()

        // show and hide the password
        showAuth.setOnCheckedChangeListener { compoundButton, checkBox ->
            if (checkBox) {
                signInAuthCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
            else {
                signInAuthCode.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // sign in button
        val signInButton : Button = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener{
            val username = signInUsername.text.toString()
            val password = signInUsername.text.toString()

            // jika validasi benar
            if(validatingInput(username, password)){
                signIn(username,password)
            }
        }

        // tombol kembali
        val backButton : ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

    }

    private fun validatingInput(username: String, password: String): Boolean {

        // ERROR : kotak username harus diisi
        if(username.isEmpty()){
            signInUsername.error = "Kotak username harus diisi"
            signInUsername.requestFocus()
        }

        // ERROR : kotak kode otentikasi harus diisi
        if(password.isEmpty()){
            signInAuthCode.error = "Kode otentikasi harus diisi"
        }

        // SUCCESS : jika username dan password telah memenuhi
        if(!(username.isEmpty() && password.isEmpty())){

            val password_int = password.toInt()

            // validasi benar jika jumlah password adalah 6 digit
            if(password_int >= 100000){
                return true
            }

            // ERROR: kode otentikasi harus 6 digit
            signInAuthCode.error = "Kode otentikasi harus 6 digit"

        }

        return false
    }

    private fun signIn(username: String, password: String) {

        // gabungkan username dan password -> hash setelahnya
        val handshake_token = username + password

        // dirubah menjadi object
        val data = hashMapOf("handshake_token" to handshake_token)

        // inisialisasi fungsi firebase
        functions = FirebaseFunctions.getInstance()
        auth      = FirebaseAuth.getInstance()

        functions
            .getHttpsCallable("SignInInterface")
            .call(data)
            .addOnFailureListener {
                Log.wtf("FirebaseSignIn", it)
            }
            .addOnSuccessListener {
                Log.i("FirebaseSignIn", it.data.toString())
                val gson = Gson()
                val tokenization_data = gson.fromJson(it.data.toString(), JsonObjectSignInToken::class.java)
                Log.d("FirebaseSignIn", tokenization_data.token.toString())
                customToken = tokenization_data.token.toString()
                customToken?.let {
                    auth.signInWithCustomToken(it)
                        .addOnCompleteListener {Task ->
                            if(Task.isSuccessful){
                                Log.d("FirebaseSignIn", "signInWithCustomToken:success")
                                val user = auth.currentUser
                            }
                            else{
                                Log.w("FirebaseSignIn", "signInWithCustomToken:failure", Task.exception)
                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener{
                            Log.wtf("FirebaseSignIn", it)
                        }
                }
            }
    }

}