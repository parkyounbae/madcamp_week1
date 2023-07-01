package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    val binding by lazy {ActivityQuizBinding.inflate(layoutInflater)}

    private var currentIndex = 0
    private var numberOfQuiz = 0
    private var correctNumber = 0
    private lateinit var quizList: ArrayList<ImageData>
    private lateinit var imageView: ImageView
    private lateinit var submitText: EditText
    private lateinit var currentResultTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_quiz)
        setContentView(binding.root)

        quizList = intent.getSerializableExtra("quizList") as ArrayList<ImageData>
        numberOfQuiz = quizList.size

        imageView = findViewById(R.id.quizImageView)
        submitText = findViewById(R.id.submitText)
        currentResultTextView = findViewById(R.id.currentResult)
        currentResultTextView.text = correctNumber.toString() + "/" + numberOfQuiz.toString()
        updateImageView()

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val inputText = submitText.text.toString()
            checkAnswer(inputText)
            submitText.setText(null)
        }

        val giveUpButton = findViewById<Button>(R.id.giveUpButton)
        giveUpButton.setOnClickListener {
            endQuiz()
        }

    }

    private fun checkAnswer(answer: String) {
        // todo
        if (answer == quizList.get(currentIndex).name) {
            correct()
        } else {
            wrong()
        }
    }

    private fun correct() {
        Toast.makeText(this, "정답입니다!",Toast.LENGTH_LONG).show()
        currentIndex++
        correctNumber++
        if(currentIndex == numberOfQuiz ) {
            endQuiz()
        }
        updateCorrectText()
        updateImageView()
    }

    private fun wrong() {
        Toast.makeText(this, "틀렸습니다. 이분은 " + quizList.get(currentIndex).name + " 님입니다. 사과하세요",Toast.LENGTH_LONG).show()
        currentIndex++
        if(currentIndex  == numberOfQuiz) {
            endQuiz()
        }
        updateImageView()
    }

    private fun updateImageView() {
        imageView.setImageResource(quizList.get(currentIndex).resId)
    }

    private fun updateCorrectText() {
        currentResultTextView.text = correctNumber.toString() + "/" + numberOfQuiz.toString()
    }

    private fun endQuiz() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("correct", correctNumber)
        intent.putExtra("number", numberOfQuiz)
        startActivity(intent)
        finish()
    }
}