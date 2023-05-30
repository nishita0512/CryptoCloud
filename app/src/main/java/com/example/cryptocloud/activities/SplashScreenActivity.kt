package com.example.cryptocloud.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptocloud.R


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)

                    val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(i)

                    finish()
                } catch (e: Exception) {
                }
            }
        }
        background.start()

    }
}