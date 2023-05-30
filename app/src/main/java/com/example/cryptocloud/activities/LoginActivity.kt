package com.example.cryptocloud.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.cryptocloud.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var mauth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = Firebase.auth

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        sharedPreferences = EncryptedSharedPreferences.create(
            "CryptoCloudSharedPreferences",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


        binding.apply{
            btnLogin.setOnClickListener {

                val email = edtTxtEmailLoginActivity.text.toString().trim()
                val password = edtTxtPasswordLoginActivity.text.toString().trim()

                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        if(!mauth.currentUser?.isEmailVerified!!){
                            Toast.makeText(this@LoginActivity,"Please check your email inbox/spam folder to first verify your email",Toast.LENGTH_LONG).show()
                            mauth.signOut()
                        }
                        else{
                            sharedPreferences.edit().putString("userPassword",password).apply()
                            Intent(this@LoginActivity, MainActivity::class.java).also { i ->
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(i)
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity,"Failed to Login",Toast.LENGTH_LONG).show()
                    }
                }

            }
            txtCreateAccountLoginActivity.setOnClickListener {
                Intent(this@LoginActivity, SignUpActivity::class.java).also {
                    startActivity(it)
                }
            }
            txtForgotPasswordLoginActivity.setOnClickListener {
                Intent(this@LoginActivity, ForgotPasswordActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }
}