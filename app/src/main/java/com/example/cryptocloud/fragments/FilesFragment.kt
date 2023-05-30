package com.example.cryptocloud.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.cryptocloud.R
import com.example.cryptocloud.activities.LoginActivity
import com.example.cryptocloud.adapters.CryptoFilesAdapter
import com.example.cryptocloud.databinding.FragmentFilesBinding
import com.example.cryptocloud.models.CryptoFile
import com.example.cryptocloud.util.AES
import com.example.cryptocloud.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class FilesFragment : Fragment() {

    private lateinit var binding: FragmentFilesBinding
    private lateinit var cryptoFilesList: ArrayList<CryptoFile>
    private var tempCryptoFilesList = ArrayList<CryptoFile>()
    private lateinit var cryptoFilesAdapter: CryptoFilesAdapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorageReference: StorageReference
    private lateinit var mauth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilesBinding.inflate(inflater)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorageReference = FirebaseStorage.getInstance().reference
        mauth = FirebaseAuth.getInstance()

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        sharedPreferences = EncryptedSharedPreferences.create(
            "CryptoCloudSharedPreferences",
            masterKeyAlias,
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val activityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showPasswordDialog(result.data?.data!!)
            }
        }

        binding.apply{
            recyclerViewFilesFragment.layoutManager = LinearLayoutManager(requireActivity())
            btnRefreshFilesFragment.setOnClickListener {
                txtSortFilesFragment.text = "sort"
                getFilesFromFirebase()
            }
            btnAddFilesFragment.setOnClickListener {
                Intent().also{
                    it.type = "*/*"
                    it.action = Intent.ACTION_GET_CONTENT
                    activityResultLauncher.launch(Intent.createChooser(it,"Select File"))
                }
            }
            txtSortFilesFragment.setOnClickListener {
                val popup = PopupMenu(context, imgSortFilesFragment)
                popup.inflate(R.menu.sort_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.byNewestFirstSortMenu -> {
                            cryptoFilesList.sortWith { o1, o2 -> if (o1?.timeCreated!! < o2?.timeCreated!!) 1 else -1 }
                            cryptoFilesAdapter.notifyDataSetChanged()
                            txtSortFilesFragment.text = "Newest First"
                            true
                        }
                        R.id.byOldestFirstSortMenu -> {
                            cryptoFilesList.sortWith { o1, o2 -> if (o1?.timeCreated!! > o2?.timeCreated!!) 1 else -1 }
                            cryptoFilesAdapter.notifyDataSetChanged()
                            txtSortFilesFragment.text = "Oldest First"
                            true
                        }
                        R.id.byNameAscendingSortMenu -> {
                            cryptoFilesList.sortWith { o1, o2 -> o1.name.compareTo(o2.name,true) }
                            cryptoFilesAdapter.notifyDataSetChanged()
                            txtSortFilesFragment.text = "A->Z"
                            true
                        }
                        R.id.byNameDescendingSortMenu -> {
                            cryptoFilesList.sortWith { o1, o2 -> o1.name.compareTo(o2.name,true) }
                            cryptoFilesList.reverse()
                            cryptoFilesAdapter.notifyDataSetChanged()
                            txtSortFilesFragment.text = "Z->A"
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                popup.show()
            }
            imgSortFilesFragment.setOnClickListener {
                txtSortFilesFragment.callOnClick()
            }
            edtTxtSearchFilesFragment.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    search(s.toString())
                }

            })
        }

        cryptoFilesList = ArrayList()
        getFilesFromFirebase()

        return binding.root
    }

    private fun search(searchText: String) {
        if(searchText.isBlank() or searchText.isEmpty()){
            cryptoFilesList.clear()
            cryptoFilesList.addAll(tempCryptoFilesList)
            cryptoFilesAdapter.notifyDataSetChanged()
        }
        else{
            cryptoFilesList.clear()
            cryptoFilesList.addAll(tempCryptoFilesList)
            val removeList = ArrayList<CryptoFile>()
            cryptoFilesList.forEachIndexed { index, cryptoFile ->
                if (!(cryptoFile.name.lowercase()).contains(searchText.lowercase())) {
                    removeList.add(cryptoFile)
                }
            }
            removeList.forEach {
                cryptoFilesList.remove(it)
            }
            cryptoFilesAdapter.notifyDataSetChanged()
        }
    }

    private fun uploadFileToFirebase(data: Uri,password: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val cursor = requireActivity().contentResolver.query(data, null, null, null, null)
            val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor?.moveToFirst()
            val fileName = cursor?.getString(nameIndex!!)!!
            var fileType = requireActivity().contentResolver.getType(data).toString()
            fileType = fileType.substring(fileType.lastIndexOf('/')+1,fileType.length)
            Log.d("Nishita","File Type: $fileType")

            val file = getFile(requireActivity().applicationContext,data)
            Log.d("Nishita","File: ${file.absolutePath}")

            if(fileType=="mpeg" && file.absolutePath.contains("m4a")){
                fileType="m4a"
            }

            var temp = password
            while((temp.length!=16) and (temp.length!=24) and (temp.length<32)){
                temp+="a"
            }

            val fileSize = file.length()

            withContext(Dispatchers.Main) {

                try {

                    firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).get().addOnSuccessListener { userSnapShot ->

                        val todaysUsage = userSnapShot.get("todays_usage").toString().toLong()

                        if(todaysUsage+fileSize>100000000){
                            Toast.makeText(requireContext(),"Not Enough Daily Limit Available",Toast.LENGTH_LONG).show()
                            binding.progressBarLayoutFilesFragment.visibility = View.GONE
                        }
                        else{

                            val encodedFileByteArray = AES.encrypt(getFile(requireActivity().applicationContext,data).readBytes(),temp.toByteArray())
                            val encodedFile = File("${file.absolutePath}1")

                            if (!encodedFile.exists()) {
                                encodedFile.createNewFile()
                            }
                            val fos = FileOutputStream(encodedFile)
                            fos.write(encodedFileByteArray)
                            fos.close()

                            val reference = firebaseStorageReference.child("${Constants.userName}${System.currentTimeMillis()}.$fileType")

                            binding.progressBarLayoutFilesFragment.visibility = View.VISIBLE

                            reference.putFile(Uri.fromFile(encodedFile)).addOnCompleteListener { uploadFileCompleteListener ->
                                if(uploadFileCompleteListener.isSuccessful){
                                    val uriTask = uploadFileCompleteListener.result.storage.downloadUrl
                                    uriTask.addOnCompleteListener {

                                        if(encodedFile.exists()){
                                            encodedFile.delete()
                                        }
                                        val url = uriTask.result
                                        val fileMap = HashMap<String,Any>()
                                        fileMap["name"] = fileName
                                        fileMap["file_type"] = fileType
                                        fileMap["time_created"] = System.currentTimeMillis()
                                        fileMap["url"] = url.toString()
                                        fileMap["username"] = Constants.userName
                                        fileMap["hashed_password"] = Constants.md5(password)

                                        firebaseFirestore.collection("files").add(fileMap).addOnCompleteListener { addDocCompleteListener ->

                                            val userMap = HashMap<String, Any>()
                                            userMap["last_uploaded"] = System.currentTimeMillis()
                                            userMap["todays_usage"] = todaysUsage+fileSize

                                            firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).update(userMap).addOnCompleteListener {
                                                if(addDocCompleteListener.isSuccessful){
                                                    binding.apply{
                                                        progressBarLayoutFilesFragment.visibility = View.GONE
                                                        btnRefreshFilesFragment.callOnClick()
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(requireContext(),"Some Error Occurred In Uploading",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                                else{
                                    Log.d("Error",uploadFileCompleteListener.exception?.localizedMessage.toString()+uploadFileCompleteListener.exception?.cause)
                                    uploadFileCompleteListener.exception?.printStackTrace()
                                    binding.progressBarLayoutFilesFragment.visibility = View.GONE
                                    Toast.makeText(requireContext(),"Some Error Occurred In Uploading",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("Nishita", e.message!!)
                    binding.progressBarLayoutFilesFragment.visibility = View.GONE
                    Toast.makeText(requireContext(),"Some Error Occurred In Uploading",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun getFilesFromFirebase(){

        if(mauth.currentUser==null){
            Intent(requireContext(),LoginActivity::class.java).also{
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        }

        binding.progressBarLayoutFilesFragment.visibility = View.VISIBLE

        firebaseFirestore.collection("files").get().addOnCompleteListener { filesGetSnapshot->
            if(filesGetSnapshot.isSuccessful){

                firebaseFirestore.collection("users").document(mauth.currentUser?.uid!!).get().addOnCompleteListener { userGetSnapshot->

                    if(userGetSnapshot.isSuccessful){
                        val todaysUsage = userGetSnapshot.result.data?.get("todays_usage").toString().toLong()
                        val doubleTodaysUsage = todaysUsage/ 1000000.toFloat()
                        binding.apply {
                            txtUserDataLimit.text = "Todays Usage: $doubleTodaysUsage / 100 MB"
                            userDataLimitProgressBarFilesFragment.progress = doubleTodaysUsage.toInt()
                            progressBarLayoutFilesFragment.visibility = View.GONE
                        }
                    }
                    else{
                        Toast.makeText(requireContext(),"Some Error Occurred While retrieving Data Usage",Toast.LENGTH_SHORT).show()
                    }

                }

                cryptoFilesList.clear()
                tempCryptoFilesList.clear()
                for(document in filesGetSnapshot.result){
                    val file = CryptoFile(document.getString("name")!!,document.getString("url")!!,document.getString("file_type")!!,document.getLong("time_created")!!,document.getString("username")!!,document.getString("hashed_password")!!)
                    cryptoFilesList.add(file)
                }
            }
            else{
                Toast.makeText(requireContext(),"Some Error Occurred While retrieving files",Toast.LENGTH_SHORT).show()
            }
            cryptoFilesAdapter = CryptoFilesAdapter(cryptoFilesList,requireActivity(),binding)
            tempCryptoFilesList.addAll(cryptoFilesList)
            binding.apply{
                recyclerViewFilesFragment.adapter = cryptoFilesAdapter
            }
        }

    }

    private fun showPasswordDialog(data: Uri){
        val builder = AlertDialog.Builder(context)
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.text_input_password, view as ViewGroup?, false)
        val input = viewInflated.findViewById<EditText>(R.id.input)
        val btnNext = viewInflated.findViewById<TextView>(R.id.btnNext)
        builder.setView(viewInflated)
        val dialog = builder.create()

        btnNext.setOnClickListener {
            dialog.dismiss()
            binding.progressBarLayoutFilesFragment.visibility = View.VISIBLE
            uploadFileToFirebase(data,input.text.toString())
        }

        dialog.show()
    }

    private fun getFile(context: Context, uri: Uri): File {
        val destinationFilename = File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

}