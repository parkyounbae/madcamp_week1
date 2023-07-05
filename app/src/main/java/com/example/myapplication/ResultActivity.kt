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
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.Locale
import java.util.concurrent.TimeUnit

class ResultActivity : AppCompatActivity() {
    val binding by lazy { ActivityResultBinding.inflate(layoutInflater)}

    private var correctNumber = 0
    private var numberOfQuiz = 0
    private lateinit var ResultTextView: TextView
    private lateinit var MessageText: TextView
    private lateinit var viewKonfetti: KonfettiView
    private lateinit var TimeTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
        setContentView(binding.root)

        numberOfQuiz = intent.getSerializableExtra("number") as Int
        correctNumber = intent.getSerializableExtra("correct") as Int
        val totalTime = intent.getSerializableExtra("timetaken") as Long
        ResultTextView = findViewById(R.id.result)
        TimeTextView = findViewById(R.id.stopwatch)
        MessageText = findViewById(R.id.message)
        ResultTextView.text = correctNumber.toString() + " / " + numberOfQuiz.toString()

        viewKonfetti = findViewById<KonfettiView>(R.id.konfettiView)

        showResultMessage(correctNumber, numberOfQuiz)

        val timeText = formatTime(totalTime)
        TimeTextView.text = "걸린 시간\n" + timeText

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
            MessageText.text = "\uD83D\uDE1A\uD83D\uDE1D\uD83E\uDD73 축하합니다! \uD83D\uDE0D\uD83E\uDD70\uD83D\uDE04"
        } else if (score >= 0.5 * total) {
            MessageText.text = "좀 치네? 조금 더 노력해줘 \uD83E\uDD79"
        } else if (score >= 0.25 * total) {
            MessageText.text = "흠.. 좀 아쉽네 ^^ \uD83E\uDD14"
        } else {
            MessageText.text = "넌 집 가라 ㅉㅉ \uD83D\uDE21"
        }
        val party = Party(
            speed = 30f,
            maxSpeed = 50f,
            damping = 0.9f,
            angle = Angle.BOTTOM,
            spread = 90,
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 3000L,
            rotation = Rotation(),
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(50),
            position = Position.Relative(0.5, 0.0)
        )
        viewKonfetti.start(party)
    }

    private fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val milliseconds = (timeInMillis / 10) % 100
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds)
    }



}