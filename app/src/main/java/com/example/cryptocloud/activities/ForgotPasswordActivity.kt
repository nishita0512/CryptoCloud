package com.example.cryptocloud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptocloud.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()

        binding.apply{
            btnSubmitForgotPasswordActivity.setOnClickListener {

                val email = edtTxtEmailForgotPasswordActivity.text.toString().trim()
                mauth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity,"Password Reset Email is sent to your email",Toast.LENGTH_LONG).show()
                        Intent(this@ForgotPasswordActivity, LoginActivity::class.java).also{ i->
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                        }
                    }
                    else{
                        Toast.makeText(this@ForgotPasswordActivity,"Failed to send Password Reset Email to your email",Toast.LENGTH_LONG).show()
                    }
                }


            }
            txtLoginForgotPasswordActivity.setOnClickListener {
                Intent(this@ForgotPasswordActivity, LoginActivity::class.java).also{
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(it)
                }
            }
        }

    }
}