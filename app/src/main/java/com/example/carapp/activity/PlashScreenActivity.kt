package com.example.carapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carapp.R

class PlashScreenActivity : AppCompatActivity() {
    private lateinit var imgCar: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plash_screen)
        supportActionBar?.hide()
        imgCar = findViewById(R.id.img_splash_icon)
        imgCar.setImageResource(R.mipmap.car_plash_screen_icon)
        homePage()
    }
    private fun homePage() {
        Handler().postDelayed({
            val mIntent = Intent(this@PlashScreenActivity, MainActivity::class.java)
            startActivity(mIntent)
            finish()
        },3000)
    }
}