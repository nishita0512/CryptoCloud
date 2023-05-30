package com.example.cryptocloud.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.cryptocloud.databinding.ActivitySignUpBinding
import com.example.cryptocloud.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mauth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = Firebase.auth
        firebaseFirestore = FirebaseFirestore.getInstance()

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        sharedPreferences = EncryptedSharedPreferences.create(
            "CryptoCloudSharedPreferences",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        binding.apply{
            btnSignUp.setOnClickListener {

                firebaseFirestore.collection("users").get().addOnCompleteListener {

                    if(it.isSuccessful){

                        val size = it.result.size()

                        Log.d("Users Number: ",size.toString())

                        if(size<=10){
                            val name = edtTxtNameSignUpActivity.text.toString().trim()
                            val email = edtTxtEmailSignUpActivity.text.toString().trim()
                            val password = edtTxtPasswordSignUpActivity.text.toString().trim()
                            val confPassword = edtTxtConfirmPasswordSignUpActivity.text.toString().trim()

                            if(password == confPassword){
                                if(name.isNotBlank() && email.isNotBlank() && password.isNotBlank()){
                                    if(password.length>5){
                                        mauth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(this@SignUpActivity) { task ->
                                                if (task.isSuccessful) {
                                                    Log.d("Nishita", "createUserWithEmail:success")
                                                    val user = mauth.currentUser
                                                    signUpSuccessful(user,name,password)
                                                }
                                                else {
                                                    Log.w("Nishita", "createUserWithEmail:failure", task.exception)
                                                    if(task.exception?.message=="The email address is already in use by another account."){
                                                        Toast.makeText(this@SignUpActivity, "Email Id is already Registered", Toast.LENGTH_SHORT).show()
                                                    }
                                                    else{
                                                        Toast.makeText(this@SignUpActivity, "Sign Up failed.", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                    }
                                    else{
                                        Toast.makeText(this@SignUpActivity, "Password should be at least 6 letters", Toast.LENGTH_SHORT).show()
                                    }

                                }
                                else{
                                    Toast.makeText(this@SignUpActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(this@SignUpActivity, "Password and ConfirmPassword Don't match", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this@SignUpActivity,"We have currently reached our user limit",Toast.LENGTH_LONG).show()
                        }

                    }
                    else{
                        Toast.makeText(this@SignUpActivity,"Some Error Occurred while signing in, Please Check your internet connection",Toast.LENGTH_LONG).show()
                    }

                }

            }
            txtAlreadyHaveAnAccountSignUpActivity.setOnClickListener {
                Intent(this@SignUpActivity,LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }

    private fun signUpSuccessful(user: FirebaseUser?,name: String,password: String) {
        val userMap = HashMap<String,Any>()
        userMap["name"] = name
        userMap["email"] = user?.email!!
        userMap["image"] = ""
        userMap["password"] = Constants.md5(password)
        userMap["last_uploaded"] = System.currentTimeMillis()
        userMap["todays_usage"] = 0
        firebaseFirestore.collection("users").document(user.uid).set(userMap).addOnCompleteListener { addTask ->
            if(addTask.isSuccessful){
                sendEmailVerification(user)
            }
            else{
                Toast.makeText(this@SignUpActivity,"Some Error Occurred while signing in, Please Check your internet connection",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mauth.signOut()
                Toast.makeText(this,"Sign Up Successful, Please check your email inbox for verification",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this,"Failed to send Email Verification",Toast.LENGTH_SHORT).show()
                mauth.currentUser?.delete()
                finish()
                startActivity(intent)
            }
        }
    }


    override fun onBackPressed() {
        finishAffinity()
    }

}