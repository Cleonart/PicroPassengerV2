package com.example.picro_passenger.preuse_activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.picro_passenger.R
import com.example.picro_passenger.activities.ActivityMain
import com.example.picro_passenger.support.HashUtils
import com.example.picro_passenger.support.JsonObjectSignInToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson

class ActivitySignIn : AppCompatActivity(){

    lateinit var functions: FirebaseFunctions
    lateinit var auth: FirebaseAuth
    private var customToken: String? = null

    lateinit var signInUsername : EditText
    lateinit var signInAuthCode : EditText
    lateinit var showAuth : CheckBox
    lateinit var signInButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar!!.hide()

        signInUsername = findViewById(R.id.sign_in_username)
        signInAuthCode = findViewById(R.id.sign_in_auth_code)
        showAuth       = findViewById(R.id.show_auth)
        signInButton   = findViewById(R.id.sign_in_button)

        // menampilkan dan memunculkan password
        showAuth.setOnCheckedChangeListener { compoundButton, checkBox ->
            if (checkBox) {
                signInAuthCode.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else {
                signInAuthCode.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // sign in button
        signInButton.setOnClickListener{
            val username = signInUsername.text.toString()
            val password = signInAuthCode.text.toString()

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

    // passed test
    fun validatingInput(username: String = "", password: String = ""): Boolean {

        // ERROR : kotak username harus diisi
        if(username == ""){
            signInUsername.error = "Kotak username harus diisi"
            signInUsername.requestFocus()
        }

        // ERRROR : kotak kode otentikasi harus diisi
        if(password == ""){
            signInAuthCode.error = "Kode otentikasi harus diisi"
        }

        // SUCCESS : jika username dan password telah memenuhi
        if(!(username == "" && password == "")){

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
        val handshake_token = (username + password)

        // dirubah menjadi object
        val data = hashMapOf("handshake_token" to handshake_token)

        // inisialisasi fungsi firebase
        functions = FirebaseFunctions.getInstance()
        auth      = FirebaseAuth.getInstance()

        // fungsi untuk melakukan sign in
        functions
            .getHttpsCallable("SignInInterface")
            .call(data)
            .addOnFailureListener {
                Log.wtf("FirebaseSignIn", it)
                Toast.makeText(applicationContext, "Oops, terjadi kesalahan, silahkan coba lagi nanti", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Log.i("FirebaseSignIn", it.data.toString())
                val gson = Gson()
                val response = gson.fromJson(it.data.toString(), JsonObjectSignInToken::class.java)

                val token_status = response.status.toString()
                customToken = response.token.toString()

                // ERRROR : username dan password tidak cocok
                if(token_status == "USERNAME_AND_PASSWORD_NOT_MATCH"){
                    //signInUsername.error = "Username salah"
                    //signInUsername.requestFocus()
                    //signInAuthCode.error = "Kode otentikasi salah"
                }

                // SUCCESS : jika username dan password cocok
                else if(token_status == "SUCCESS"){
                    customToken?.let {
                        auth.signInWithCustomToken(it)
                            .addOnCompleteListener { Task ->

                                // jika token terautentikasi dengan benar
                                if (Task.isSuccessful) {
                                    Log.d("FirebaseSignIn", "signInWithCustomToken:success")
                                    val user = auth.currentUser
                                    val intent_to = Intent(this, ActivityMain::class.java)
                                    finish()
                                    startActivity(intent_to)
                                }

                                // jika token bermasalah
                                else {
                                    Log.w(
                                        "FirebaseSignIn",
                                        "signInWithCustomToken:failure",
                                        Task.exception
                                    )
                                    Toast.makeText(
                                        baseContext,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener {
                                Log.wtf("FirebaseSignIn", it)
                                Toast.makeText(applicationContext, "Oops, terjadi kesalahan, silahkan coba lagi nanti", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
    }

}