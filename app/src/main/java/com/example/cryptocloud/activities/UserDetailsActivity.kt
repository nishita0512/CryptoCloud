package com.example.cryptocloud.activities

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.cryptocloud.R
import com.example.cryptocloud.databinding.ActivityUserDetailsBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserDetailsBinding
    private lateinit var mauth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mauth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorageReference = FirebaseStorage.getInstance().reference

        binding.apply {

            firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        txtUserNameUserDetailsActivity.text = it.result["name"].toString()
                        txtEmailUserDetailsActivity.text = it.result["email"].toString()
                        val image = it.result["image"].toString()
                        Glide.with(this@UserDetailsActivity).load(image)
                            .error(R.drawable.img_add_image).into(imgUserDetailsActivity)
                    } else {
                        Toast.makeText(
                            this@UserDetailsActivity,
                            "Failed to Retrieve Information, Please check your internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            val activityResultImagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    val resultCode = result.resultCode
                    val data = result.data

                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val uri = data?.data!!
                            uploadAndSetImage(uri)
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toast.makeText(
                                this@UserDetailsActivity,
                                ImagePicker.getError(data),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this@UserDetailsActivity,
                                "Task Cancelled",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }

            imgUserDetailsActivity.setOnClickListener {
                ImagePicker.with(this@UserDetailsActivity)
                    .crop()
                    .compress(512)
                    .maxResultSize(512, 512)
                    .createIntent { intent ->
                        activityResultImagePicker.launch(intent)
                    }
            }
        }
    }

    private fun uploadAndSetImage(uri: Uri) {

        val fileType = contentResolver.getType(uri)

        val reference = firebaseStorageReference.child("${System.currentTimeMillis()}.$fileType")
        reference.putFile(uri).addOnCompleteListener { uploadFileCompleteListener ->
            if(uploadFileCompleteListener.isSuccessful){
                val uriTask = uploadFileCompleteListener.result.storage.downloadUrl
                uriTask.addOnCompleteListener {
                    val url = uriTask.result

                    firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).update("image",url.toString()).addOnCompleteListener { addImageCompleteListener ->
                        if(addImageCompleteListener.isSuccessful){
                            binding.apply{
                                Glide.with(this@UserDetailsActivity).load(uri).error(R.drawable.img_add_image).into(imgUserDetailsActivity)
                            }
                        }
                        else{
                            Toast.makeText(this,"Some Error Occurred While Uploading Image",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            else{
                Log.d("Error",uploadFileCompleteListener.exception?.message!!)
                Toast.makeText(this,"Some Error Occurred While Uploading Image",Toast.LENGTH_SHORT).show()
            }
        }
    }
}