package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {

    val binding by lazy {ActivityContactBinding.inflate(layoutInflater)}

    private var itemList = mutableListOf<ContactData>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val view = layoutInflater.inflate(R.layout.activity_contact, null)

        val detailName = findViewById<TextView>(R.id.detailContactName)
        val detailImage = findViewById<ImageView>(R.id.detailContactImageView)
        val detailNumber = findViewById<TextView>(R.id.detailContactNumber)

        val detailEmail = findViewById<TextView>(R.id.detailContactEmail)
        val detailInstagram = findViewById<TextView>(R.id.detailContactInstagram)
        val detailClose = findViewById<Button>(R.id.detailContactClose)
        val detailRemove = findViewById<Button>(R.id.contactRemove)
        val detailEdit = findViewById<Button>(R.id.editButton)

        //받아오기
        val name = intent.getSerializableExtra("name") as String
        itemList = MyApplication.prefs.getContact()
        val index = itemList.indexOfFirst { it.name == name }
        val contactData = itemList.get(index)

        detailName.setText(itemList.get(index).name)
        detailImage.setImageURI(Uri.parse(contactData.imageResId))
        detailNumber.text = contactData.number
        if (contactData.email == "") {
            detailEmail.text = "Email"
            detailEmail.setTextColor(R.color.gray)
        } else {
            detailEmail.text = contactData.email
        }

        if (contactData.instagram == "") {
            detailInstagram.text = "Instagram"
            detailInstagram.setTextColor(R.color.gray)
        } else {
            detailInstagram.text = contactData.instagram
        }

        //연락처 삭제 코드
        detailRemove.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setMessage("연락처를 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    itemList.removeAt(itemList.indexOf(contactData))
                    MyApplication.prefs.setContact(itemList)

                    val intent = Intent(this, NaviActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }

        detailClose.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        detailEdit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("value", 2)
            intent.putExtra("contacts", contactData.name)
            startActivity(intent)
        }



        detailNumber.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            var realNumber = "tel:"
            var tempNumber = contactData.number.split("-")
            realNumber = realNumber + tempNumber.get(0)
            realNumber = realNumber + tempNumber.get(1)
            realNumber = realNumber + tempNumber.get(2)

            Log.d("number", realNumber)
            intent.data = Uri.parse(realNumber)
            startActivity(intent)
        }

        detailInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val insta = contactData.instagram
            val url = "https://www.instagram.com/$insta"
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        detailEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:${contactData.email}")

            try {
                startActivity(emailIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to open email client", Toast.LENGTH_SHORT).show()
            }
        }

    }



}