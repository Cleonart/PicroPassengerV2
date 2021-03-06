package com.example.picro_passenger.preuse_activities

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.picro_passenger.R
import com.example.picro_passenger.Support.HashUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.example.picro_passenger.Support.JsonObjectSignInToken
import com.example.picro_passenger.newSupport.SharedPreferencesService

// passed 5 of 5 tests
// module on 95%
class ActivitySignIn : AppCompatActivity(){

    lateinit var functions: FirebaseFunctions
    lateinit var auth: FirebaseAuth

    // data token
    var customToken: String? = null
    var statusToken: String? = null

    lateinit var signInUsername : EditText
    lateinit var signInAuthCode : EditText
    lateinit var showAuth : CheckBox
    lateinit var signInButton : Button
    lateinit var backButton : ImageView
    lateinit var spinner : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar!!.hide()

        signInUsername = findViewById(R.id.sign_in_username)
        signInAuthCode = findViewById(R.id.sign_in_auth_code)
        showAuth       = findViewById(R.id.show_auth)
        signInButton   = findViewById(R.id.sign_in_button)
        backButton     = findViewById(R.id.back_button)
        spinner        = findViewById(R.id.loading_spinner)
        spinner.visibility = View.GONE // hide the spinner

        // menampilkan dan memunculkan password
        showAuth.setOnCheckedChangeListener { _, checkBox ->
            if (checkBox) { signInAuthCode.transformationMethod = HideReturnsTransformationMethod.getInstance() }
            else { signInAuthCode.transformationMethod = PasswordTransformationMethod.getInstance() }
        }

        // tombol masuk
        signInButton.setOnClickListener{
            val username = signInUsername.text.toString()
            val password = signInAuthCode.text.toString()

            // jika validasi benar
            if(validatingInput(username, password)){
                spinner.visibility = View.VISIBLE
                signIn(username,password)
            }
        }

        // tombol kembali
        backButton.setOnClickListener{
            finish()
        }

    }

    // passed test
    fun validatingInput(username: String = "", password: String = ""): Boolean {

        if(username == "") {
            signInUsername.error = "Kotak username harus diisi"
            signInUsername.requestFocus()
        }

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
            signInAuthCode.requestFocus()
        }

        return false
    }

    private fun signIn(username: String, password: String) {

        // gabungkan username dan password -> hash setelahnya
        val handshake_token = (HashUtils.sha1(username + "" + password)).toLowerCase()
        Log.i("FirebaseSignIn", "Token : $handshake_token")

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
                val signInResult = Gson().toJson(it.data).toString()
                val response = Gson().fromJson(signInResult, JsonObjectSignInToken::class.java)

                val token_status = response.status.toString()
                customToken = response.token.toString()
                statusToken = token_status

                Log.i("FirebaseSignIn", customToken.toString())
                Log.i("FirebaseSignIn", statusToken.toString())

                // ERRROR : username dan password tidak cocok
                if(token_status == "USERNAME_AND_PASSWORD_NOT_MATCH"){
                    spinner.visibility = View.GONE
                    signInUsername.error = "Username tidak ditemukan"
                    signInUsername.requestFocus()
                    signInAuthCode.error = "Kode otentikasi salah"
                }

                // SUCCESS : jika username dan password cocok
                else if(token_status == "SUCCESS"){
                    customToken?.let {
                        auth.signInWithCustomToken(it)
                            .addOnCompleteListener { Task ->

                                val userType = response.userType.toString()
                                Log.d("userType", userType)
                                SharedPreferencesService.PreferencesSet(baseContext, "userType", userType)

                                // sembunyikan loading jika loading berhasil
                                spinner.visibility = View.GONE

                                // jika token terautentikasi dengan benar
                                if (Task.isSuccessful) {
                                    Log.d("FirebaseSignIn", "signInWithCustomToken:success")
                                    finish()
                                }

                                // jika token bermasalah
                                else {
                                    Log.w("FirebaseSignIn", "signInWithCustomToken:failure", Task.exception)
                                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
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