package com.example.cryptocloud.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.cryptocloud.util.Constants
import com.example.cryptocloud.activities.ForgotPasswordActivity
import com.example.cryptocloud.activities.LoginActivity
import com.example.cryptocloud.activities.UserDetailsActivity
import com.example.cryptocloud.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var mauth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)

        mauth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.apply{

            txtHelloUsernameProfileFragment.text = "Hello ${Constants.userName}"

            viewProfileCardViewProfileFragment.setOnClickListener {
                Intent(requireActivity(),UserDetailsActivity::class.java).also { i->
                    startActivity(i)
                }
            }
            changePasswordCardViewProfileFragment.setOnClickListener {
                mauth.signOut()
                Intent(requireContext(),ForgotPasswordActivity::class.java).also {
                    startActivity(it)
                }
            }
            logoutCardViewProfileFragment.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false)
                    .setPositiveButton(HtmlCompat.fromHtml("<font color='#A8B1C8'>Yes</font>", 0)) { dialog, id ->
                        mauth.signOut()
                        Toast.makeText(requireContext(),"Logged Out Successfully",Toast.LENGTH_SHORT).show()
                        Intent(requireContext(),LoginActivity::class.java).also{
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                    }
                    .setNegativeButton(HtmlCompat.fromHtml("<font color='#A8B1C8'>No</font>", 0)) {
                            dialog, id -> dialog.dismiss()
                    }
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to logout?")
                val alert = builder.create()
                alert.show()

            }
            deleteAccountCardViewProfileFragment.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false)
                    .setPositiveButton(HtmlCompat.fromHtml("<font color='#A8B1C8'>Yes</font>", 0)) { dialog, id ->
                        val uid = mauth.currentUser?.uid
                        mauth.currentUser?.delete()?.addOnCompleteListener { taskDeleteUser ->
                            if(taskDeleteUser.isSuccessful){
                                firebaseFirestore.collection("users").document(uid!!).delete().addOnCompleteListener { taskDeleteDoc ->
                                    if(taskDeleteDoc.isSuccessful){
                                        Toast.makeText(requireContext(),"Account Deleted Successfully",Toast.LENGTH_SHORT).show()
                                        Intent(requireContext(),LoginActivity::class.java).also{ i ->
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(i)
                                        }
                                    }
                                    else{
                                        Toast.makeText(requireContext(),"Failed to Delete Account",Toast.LENGTH_LONG).show()
                                    }
                                }

                            }
                            else{
                                Toast.makeText(requireContext(),"Failed to Delete Account, Try logging out and in again",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .setNegativeButton(HtmlCompat.fromHtml("<font color='#A8B1C8'>No</font>", 0)) {
                            dialog, id -> dialog.dismiss()
                    }
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to Delete your account?")
                val alert = builder.create()
                alert.show()

            }
        }

        return binding.root
    }

}