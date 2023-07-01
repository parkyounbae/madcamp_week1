package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.databinding.ActivityResultBinding
import com.example.myapplication.databinding.FragmentBlank3Binding

class ResultActivity : AppCompatActivity() {
    val binding by lazy { ActivityResultBinding.inflate(layoutInflater)}

    private var correctNumber = 0
    private var numberOfQuiz = 0
    private lateinit var ResultTextView: TextView
    private lateinit var MessageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
        setContentView(binding.root)

        numberOfQuiz = intent.getSerializableExtra("number") as Int
        correctNumber = intent.getSerializableExtra("correct") as Int
        ResultTextView = findViewById(R.id.result)
        MessageText = findViewById(R.id.message)
        ResultTextView.text = correctNumber.toString() + " / " + numberOfQuiz.toString()

        showResultMessage(correctNumber, numberOfQuiz)

//        val binding = ActivityResultBinding.inflate(layoutInflater)

        val EndButton = findViewById<Button>(R.id.endButton)

        EndButton.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun showResultMessage(score: Int, total: Int) {
        if (score >= 0.75 * total) {
            MessageText.text = "축하합니다!"
        } else if (score >= 0.5 * total) {
            MessageText.text = "잘 하셨습니다! 좋은 성적입니다."
        } else if (score >= 0.25 * total) {
            MessageText.text = "노력이 보이는 성적입니다. 계속 열심히 하세요."
        } else {
            MessageText.text = "분발하세요! 더 많은 노력이 필요합니다."
        }
    }



}