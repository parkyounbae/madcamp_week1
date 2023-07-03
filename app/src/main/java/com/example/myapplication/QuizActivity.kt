package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityQuizBinding
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class QuizActivity : AppCompatActivity() {
    val binding by lazy {ActivityQuizBinding.inflate(layoutInflater)}

    private var currentIndex = 0
    private var numberOfQuiz = 0
    private var correctNumber = 0
    private lateinit var quizList: MutableList<ContactData>
    private lateinit var imageView: ImageView
    private lateinit var submitText: EditText
    private lateinit var currentResultTextView: TextView

    private lateinit var timer: CountDownTimer
    private var elapsedTime: Long = 0
    private var totalTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_quiz)
        setContentView(binding.root)

        quizList = MyApplication.prefs.getContact()


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

        startTimer()

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
        Toast.makeText(this, "정답입니다!",Toast.LENGTH_SHORT).show()
        currentIndex++
        correctNumber++
        if(currentIndex == numberOfQuiz ) {
            endQuiz()
        }
        updateCorrectText()
        updateImageView()
    }

    private fun wrong() {
        Toast.makeText(this, "틀렸습니다. 이분은 " + quizList.get(currentIndex).name + " 님입니다. 사과하세요",Toast.LENGTH_SHORT).show()
        currentIndex++
        if(currentIndex  == numberOfQuiz) {
            endQuiz()
        }
        updateImageView()
    }

    private fun updateImageView() {
        val resourceId = resources.getIdentifier("@drawable/"+quizList.get(currentIndex).imageResId, "drawable", "com.example.myapplication")
        imageView.setImageResource(resourceId)
        cropImageToSquare(imageView)
        imageView.setBackgroundResource(R.drawable.image_corner)
        imageView.clipToOutline = true
    }

    fun cropImageToSquare(imageView: ImageView) {
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val width = bitmap.width
            val height = bitmap.height

            val size = if (width > height) height else width
            val left = (width - size) / 2
            val top = (height - size) / 2

            val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, size, size)
            imageView.setImageBitmap(croppedBitmap)
        }
    }

    private fun updateCorrectText() {
        currentResultTextView.text = correctNumber.toString() + "/" + numberOfQuiz.toString()
    }

    private fun endQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        totalTime = elapsedTime
        intent.putExtra("correct", correctNumber)
        intent.putExtra("number", numberOfQuiz)
        intent.putExtra("timetaken", totalTime) // Pass the finalElapsedTime
        startActivity(intent)
        finish()
    }

    private fun startTimer() {
        val startTime = System.currentTimeMillis()

        timer = object : CountDownTimer(Long.MAX_VALUE, 1) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = System.currentTimeMillis() - startTime
                val timeText = formatTime(elapsedTime)
                // 타이머를 표시하는 TextView의 id를 사용하여 해당 TextView를 찾아서 업데이트합니다
                // 예시: binding.timerTextView.text = timeText
                binding.stopwatch.text = timeText
            }

            override fun onFinish() {
                // 타이머 종료 시 호출되는 메서드입니다 (이 예제에서는 사용하지 않습니다)
//                totalTime = elapsedTime
            }
        }
        timer.start()
    }


    private fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val milliseconds = (timeInMillis / 10) % 100
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds)
    }
}