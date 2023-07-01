package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

class CustomDialog(context: Context, private val title: String) : Dialog(context) {
    init {
        // CustomDialog의 레이아웃 설정
        setContentView(R.layout.activity_image_pop_up)

        // 뷰 초기화 및 동작 설정
        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            dismiss()
        }

        val titleTextView = findViewById<TextView>(R.id.dialogTitle)
        titleTextView.text = title
    }
}