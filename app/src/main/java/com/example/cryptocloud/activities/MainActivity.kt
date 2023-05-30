package com.example.cryptocloud.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cryptocloud.util.Constants
import com.example.cryptocloud.fragments.FilesFragment
import com.example.cryptocloud.fragments.ProfileFragment
import com.example.cryptocloud.R
import com.example.cryptocloud.adapters.ViewPagerAdapter
import com.example.cryptocloud.databinding.ActivityMainBinding
import com.example.cryptocloud.util.AES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mauth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = byteArrayOf(0.toByte(),1.toByte(),2.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte(),7.toByte(),8.toByte(),9.toByte(),0.toByte(),1.toByte(),2.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte())
        val key = byteArrayOf(0.toByte(),1.toByte(),2.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte(),7.toByte(),8.toByte(),9.toByte(),0.toByte(),1.toByte(),2.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte(),7.toByte(),8.toByte(),9.toByte(),0.toByte(),1.toByte(),2.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte(),7.toByte(),8.toByte(),9.toByte(),0.toByte(),1.toByte())
//        val result = AES.decrypt(AES.encrypt(data,key), key)
        val result = AES.encrypt(data,key)
        Log.d("Check", result.contentToString())

        mauth = Firebase.auth

        if(mauth.currentUser==null){
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }
        else{

            firebaseFirestore = FirebaseFirestore.getInstance()

            firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).get().addOnCompleteListener { userGetSnapShot ->
                if(userGetSnapShot.isSuccessful){
                    Constants.userName = userGetSnapShot.result["name"].toString()
                    val todaysUsage = userGetSnapShot.result["todays_usage"]?.toString()?.toLong() ?: 0L
                    val lastUploaded = userGetSnapShot.result["last_uploaded"].toString().toLong()
                    val timeNow = System.currentTimeMillis()
                    val zero: Long = 0
                    if((timeNow-lastUploaded>86400000) and (zero!=todaysUsage)){
                        firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).update("todays_usage",zero)
                    }
                }
                else{
                    Toast.makeText(this@MainActivity,"Failed to Retrieve Information, Please check your internet connection", Toast.LENGTH_LONG).show()
                }
            }

            val fragmentList = arrayListOf(
                FilesFragment(),
                ProfileFragment()
            )

            val viewPagerAdapter = ViewPagerAdapter(fragmentList,this.supportFragmentManager,lifecycle)

            binding.apply{

                viewPagerMainActivity.isUserInputEnabled = false

                bottomMenu.setItemSelected(R.id.files)
                viewPagerMainActivity.adapter = viewPagerAdapter
                bottomMenu.setOnItemSelectedListener {
                    when(it){
                        R.id.files ->{
                            viewPagerMainActivity.currentItem = 0
                        }
                        R.id.profile ->{
                            viewPagerMainActivity.currentItem = 1
                        }
                    }
                }

            }

        }



    }
}