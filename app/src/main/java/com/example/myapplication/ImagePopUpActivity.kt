package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ActivityOptions
import android.content.Intent
import android.widget.ImageView

class ImagePopUpActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pop_up)

        imageView = findViewById(R.id.imagePopup)

    }
}