package com.example.cryptocloud.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptocloud.R
import com.example.cryptocloud.databinding.ActivityPropertiesBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PropertiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name").toString()
        val size = intent.getStringExtra("size").toString()
        val timeCreated = intent.getLongExtra("timeCreated",0L)
        val fileType = intent.getStringExtra("fileType").toString()

        val time = Date(timeCreated)
        val formatter: DateFormat = SimpleDateFormat("HH:mm a dd/MM/yyyy")
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        binding.apply{
            txtFileNamePropertiesActivity.text = "Name: $name"
            txtFileSizePropertiesActivity.text = "Size: $size"
            txtFileTimeCreatedPropertiesActivity.text = "Time: ${formatter.format(time)}"
            txtFileTypePropertiesActivity.text = "File Type: $fileType"
        }

    }
}