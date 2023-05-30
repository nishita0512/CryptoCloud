package com.example.cryptocloud.adapters

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintAttribute
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.cryptocloud.BuildConfig
import com.example.cryptocloud.R
import com.example.cryptocloud.activities.PropertiesActivity
import com.example.cryptocloud.databinding.FragmentFilesBinding
import com.example.cryptocloud.models.CryptoFile
import com.example.cryptocloud.util.AES
import com.example.cryptocloud.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class CryptoFilesAdapter(private val cryptoFilesList: ArrayList<CryptoFile>,private val context: Context,private val binding: FragmentFilesBinding): RecyclerView.Adapter<CryptoFilesAdapter.CryptoFileViewHolder>() {

    var parent: ViewGroup? = null
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private var sharedPreferences = EncryptedSharedPreferences.create(
        "CryptoCloudSharedPreferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    class CryptoFileViewHolder(view:View): RecyclerView.ViewHolder(view){
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainLayoutCryptoFileSingleRow)
        val txtFileName:TextView = view.findViewById(R.id.txtFileNameSingleRow)
        val imgFile: ImageView = view.findViewById(R.id.imgFileSingleRow)
        val btnMenu: ImageView = view.findViewById(R.id.btnMenuFileSingleRow)
        val txtOwnerName: TextView = view.findViewById(R.id.txtFileOwnerNameSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoFileViewHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_file_single_row,parent,false)
        return CryptoFileViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoFileViewHolder, position: Int) {
        holder.txtFileName.text = cryptoFilesList[position].name
        holder.txtOwnerName.text = cryptoFilesList[position].ownerName
        when(cryptoFilesList[position].fileType){
            "pdf" -> holder.imgFile.setImageResource(R.drawable.img_pdf)
            "jpg" -> holder.imgFile.setImageResource(R.drawable.img_jpg)
            "jpeg" -> holder.imgFile.setImageResource(R.drawable.img_jpg)
            "png" -> holder.imgFile.setImageResource(R.drawable.img_png)
            "mp3" -> holder.imgFile.setImageResource(R.drawable.img_music)
            "m4a" -> holder.imgFile.setImageResource(R.drawable.img_music)
            "wav" -> holder.imgFile.setImageResource(R.drawable.img_music)
            "wma" -> holder.imgFile.setImageResource(R.drawable.img_music)
            "aac" -> holder.imgFile.setImageResource(R.drawable.img_music)
            "mpeg" -> holder.imgFile.setImageResource(R.drawable.img_music)
            else -> holder.imgFile.setImageResource(R.drawable.img_file)
        }

        holder.btnMenu.setOnClickListener {
            val popup = PopupMenu(context, holder.btnMenu)
            popup.inflate(R.menu.pop_up_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.openPopUpMenu -> {
                        showPasswordDialog(cryptoFilesList[position],position,0)
                        true
                    }
                    R.id.renamePopUpMenu -> {
                        showRenameDialog(cryptoFilesList[position],position)
                        true
                    }
                    R.id.downloadPopMenu -> {
                        showPasswordDialog(cryptoFilesList[position],position,1)
                        true
                    }
                    R.id.deletePopUpMenu -> {
                        showPasswordDialog(cryptoFilesList[position],position,2)
                        true
                    }
                    R.id.propertiesPopUpMenu -> {
                        showPasswordDialog(cryptoFilesList[position],position,3)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popup.show()
        }

        holder.mainLayout.setOnClickListener {
            showPasswordDialog(cryptoFilesList[position],position,0)
        }

    }

    override fun getItemCount(): Int {
        return cryptoFilesList.size
    }

    private fun showPasswordDialog(cryptoFile:CryptoFile, position: Int,option: Int){
        val builder = AlertDialog.Builder(context)
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.text_input_password, parent, false)
        val input = viewInflated.findViewById<EditText>(R.id.input)
        val btnNext = viewInflated.findViewById<TextView>(R.id.btnNext)
        builder.setView(viewInflated)
        val dialog = builder.create()

        btnNext.setOnClickListener {
            dialog.dismiss()

            val hashedPassword = Constants.md5(input.text.toString())
            if(hashedPassword!=cryptoFile.hashedPassword){
                Toast.makeText(context,"Incorrect Password, Task cancelled",Toast.LENGTH_SHORT).show()
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
            }
            else{
                binding.progressBarLayoutFilesFragment.visibility = View.VISIBLE
                when(option){
                    0->downloadFile(cryptoFile,input.text.toString(),0)
                    1->downloadFile(cryptoFile,input.text.toString(),1)
                    2->deleteFile(cryptoFile,position)
                    3->downloadFile(cryptoFile,input.text.toString(),2)
                }
            }

        }
        dialog.show()
    }

    private fun showRenameDialog(cryptoFile: CryptoFile,position: Int){
        val builder = AlertDialog.Builder(context)
        val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.text_input_rename, parent, false)
        val pass = viewInflated.findViewById<EditText>(R.id.edtTxtPassword)
        val rename = viewInflated.findViewById<EditText>(R.id.edtTxtRename)
        val btnNext = viewInflated.findViewById<TextView>(R.id.btnNext)
        builder.setView(viewInflated)
        val dialog = builder.create()

        btnNext.setOnClickListener {
            dialog.dismiss()

            val password = cryptoFile.hashedPassword
            if(password != pass.text.toString()){
                Toast.makeText(context,"Incorrect Password, renaming cancelled",Toast.LENGTH_SHORT).show()
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
            }
            else{
                renameFile(cryptoFile,rename.text.toString(),position)
            }

        }
        dialog.show()
    }

    private fun downloadFile(cryptoFile: CryptoFile, password:String,option:Int) {

        val rootPath = File(context.filesDir.path + File.separatorChar)
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val localFile = File(rootPath, "${cryptoFile.name}.${cryptoFile.fileType}")
        var temp = password
        while((temp.length!=16) and (temp.length!=24) and (temp.length<32)){
            temp+="a"
        }
        FirebaseStorage.getInstance().getReferenceFromUrl(cryptoFile.url).getFile(localFile).addOnSuccessListener {
                val decodedFile = AES.decrypt(localFile.readBytes(),temp.toByteArray())
                if(option==0){
                    openDecodedByteArray(decodedFile,cryptoFile.name)
                }
                else if(option==1){
                    saveDecodedByteArray(decodedFile,cryptoFile.name)
                }
                else{
                    showPropertiesOfFile(decodedFile,cryptoFile)
                }
            }.addOnFailureListener { exception ->
                Log.e(
                    "firebase ",
                    "local file not created  created $exception"
                )
                Toast.makeText(context,"Failed to Retrieve File",Toast.LENGTH_SHORT).show()
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
            }
    }

    private fun openDecodedByteArray(byteArray: ByteArray,name: String){
        try {
            val fileDir = File(context.getExternalFilesDir(null)?.absolutePath + "/CryptoCloud")
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            val decodedFile = File(fileDir,name)
            if (!decodedFile.exists()) {
                decodedFile.createNewFile()
            }
            val fos = FileOutputStream(decodedFile)
            fos.write(byteArray)
            fos.close()

            binding.progressBarLayoutFilesFragment.visibility = View.GONE

            val myIntent = Intent(Intent.ACTION_VIEW)
            myIntent.data = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",decodedFile)
            myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent.createChooser(myIntent, "Choose an application to open with:").also{
                context.startActivity(it)
            }

        } catch (e: Exception) {
            Log.e("Nishita", e.message!!)
            Toast.makeText(context,"Some Error Occurred In Opening File", Toast.LENGTH_SHORT).show()
            binding.progressBarLayoutFilesFragment.visibility = View.GONE
        }
    }

    private fun saveDecodedByteArray(decodedByteArray: ByteArray, name:String){
        try {
            val fileDir = File(context.getExternalFilesDir(null)?.absolutePath + "/CryptoCloud")
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            val decodedFile = File(fileDir,name)
            if (!decodedFile.exists()) {
                decodedFile.createNewFile()
            }

            val fosDecodedFile = FileOutputStream(decodedFile)
            fosDecodedFile.write(decodedByteArray)
            fosDecodedFile.close()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val mimeType = resolver.getType(FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",decodedFile))
                Log.d("Nishita","Mime Type: $mimeType")
                val contentValues = ContentValues().apply {
                    put(MediaStore.Files.FileColumns.DISPLAY_NAME, name)
                    put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
                    put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                val fos = resolver.openOutputStream(uri!!)
                val fin = FileInputStream(decodedFile)
                fin.copyTo(fos!!, 1024)
                fos.flush()
                fos.close()
                fin.close()
            } else {
                val destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + name

                val uri = Uri.parse("file://$destination")
                val fos = context.contentResolver.openOutputStream(uri!!)
                val fin = FileInputStream(decodedFile)
                fin.copyTo(fos!!, 1024)
                fos.flush()
                fos.close()
                fin.close()
            }

            Toast.makeText(context,"File saved to Downloads", Toast.LENGTH_SHORT).show()
            binding.progressBarLayoutFilesFragment.visibility = View.GONE


        } catch (e: Exception) {
            Log.e("Nishita", e.message!!)
            Toast.makeText(context,"Some Error Occurred In Opening File", Toast.LENGTH_SHORT).show()
            binding.progressBarLayoutFilesFragment.visibility = View.GONE
        }
    }

    private fun renameFile(cryptoFile: CryptoFile, newName: String, position: Int){
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("files").get().addOnCompleteListener {
            if(it.isSuccessful){
                it.result.documents.forEach { doc ->
                    if(doc.data?.get("url").toString() == cryptoFile.url){
                        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("files").document(doc.id).update("name","$newName.${cryptoFile.fileType}").addOnCompleteListener { renameTask ->
                            if(renameTask.isSuccessful){
                                cryptoFilesList.removeAt(position)
                                cryptoFilesList.add(position,
                                    CryptoFile("$newName.${cryptoFile.fileType}",cryptoFile.url,cryptoFile.fileType, cryptoFile.timeCreated, cryptoFile.ownerName, cryptoFile.hashedPassword)
                                )
                                notifyDataSetChanged()
                            }
                            else{
                                Toast.makeText(context,"Some Error Occurred While renaming",Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBarLayoutFilesFragment.visibility = View.GONE
                        }
                    }
                }
            }
            else{
                Toast.makeText(context,"Some Error Occurred While renaming",Toast.LENGTH_SHORT).show()
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
            }
        }
    }

    private fun deleteFile(cryptoFile:CryptoFile, position: Int){
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val fileReference = FirebaseStorage.getInstance().getReferenceFromUrl(cryptoFile.url)

        fileReference.delete().addOnCompleteListener {
            if(it.isSuccessful){
                firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("files").get().addOnCompleteListener { getAllDocs ->
                    if(getAllDocs.isSuccessful){
                        getAllDocs.result.documents.forEach { doc ->
                            if(doc.data?.get("url").toString() == cryptoFile.url){
                                firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("files").document(doc.id).delete().addOnCompleteListener { renameTask ->
                                    if(renameTask.isSuccessful){
                                        Toast.makeText(context,"File Deleted Successfully",Toast.LENGTH_SHORT).show()
                                        cryptoFilesList.removeAt(position)
                                        notifyDataSetChanged()
                                    }
                                    else{
                                        Toast.makeText(context,"Some Error Occurred While Deleting",Toast.LENGTH_SHORT).show()
                                    }
                                    binding.progressBarLayoutFilesFragment.visibility = View.GONE
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(context,"Some Error Occurred While Deleting",Toast.LENGTH_SHORT).show()
                        binding.progressBarLayoutFilesFragment.visibility = View.GONE
                    }
                }
            }
            else{
                Toast.makeText(context,"File Deleted Successfully",Toast.LENGTH_SHORT).show()
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
            }
        }

    }

    private fun showPropertiesOfFile(byteArray: ByteArray, cryptoFile: CryptoFile){
        try {
            val fileDir = File(context.getExternalFilesDir(null)?.absolutePath + "/CryptoCloud")
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            val decodedFile = File(fileDir,cryptoFile.name)
            if (!decodedFile.exists()) {
                decodedFile.createNewFile()
            }
            val fos = FileOutputStream(decodedFile)
            fos.write(byteArray)
            fos.close()

            binding.progressBarLayoutFilesFragment.visibility = View.GONE
            context.contentResolver.query(FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",decodedFile), null, null, null, null).use { cursor ->
                val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE)
                cursor?.moveToFirst()
                val size = cursor?.getLong(sizeIndex!!)!!/1000000
                binding.progressBarLayoutFilesFragment.visibility = View.GONE
                Intent(context,PropertiesActivity::class.java).also{
                    it.putExtra("name",cryptoFile.name)
                    it.putExtra("size",size.toString()+"MB")
                    it.putExtra("timeCreated",cryptoFile.timeCreated)
                    it.putExtra("fileType",cryptoFile.fileType)
                    context.startActivity(it)
                }
            }

        } catch (e: Exception) {
            Log.e("Nishita", e.message!!)
            Toast.makeText(context,"Some Error Occurred In Opening File", Toast.LENGTH_SHORT).show()
            binding.progressBarLayoutFilesFragment.visibility = View.GONE
        }
    }

}