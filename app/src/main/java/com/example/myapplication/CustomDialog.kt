package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
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

        // 다이얼로그의 크기를 조정합니다.
        val window = window
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT  // 원하는 너비로 설정
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT  // 원하는 높이로 설정
        window?.attributes = layoutParams
    }
}