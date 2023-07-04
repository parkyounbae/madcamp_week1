package com.example.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class EditActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBinding.inflate(layoutInflater)}

    private lateinit var dataManager: DataManager
    private lateinit var profileimageView: ImageView

    var imageUri = "@drawable/"+ R.drawable.baseline_person_outline_24
    private var itemList = mutableListOf <ContactData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val resourceId = resources.getIdentifier(R.drawable.baseline_person_outline_24.toString(), "drawable", "com.example.myapplication")
        imageUri = "android.resource://${this.packageName}/$resourceId"

        profileimageView = binding.profileImage
        val nametextView = binding.editName
        val phonetextView = binding.editPhone
        val emailtextView = binding.editEmail
        val instatextView = binding.editInstagram
        val doneButton = binding.contactdoneButton
        val cancelButton = binding.contactcancelButton

        var canDoneName = false
        var canDonePhone = false


        itemList = MyApplication.prefs.getContact()

        var isEdit = intent.getSerializableExtra("value") as Int
        var name = intent.getSerializableExtra("contacts") as String
        val index = itemList.indexOfFirst { it.name == name }

        if(isEdit == 2){
            // profileimageView.setImageURI(Uri.parse(itemList.get(index).imageResId))
            Glide.with(this).load(Uri.parse(itemList.get(index).imageResId)).circleCrop().into(profileimageView)
            imageUri = itemList.get(index).imageResId
            nametextView.setText(itemList.get(index).name)
            phonetextView.setText(itemList.get(index).number)
            emailtextView.setText(itemList.get(index).email)
            instatextView.setText(itemList.get(index).instagram)
            canDoneName = true
            canDonePhone = true
        } else {
            Glide.with(this).load(Uri.parse(imageUri)).circleCrop().into(profileimageView)
            canDoneName = false
            canDonePhone = false
            doneButton.isEnabled = false
        }

        profileimageView.setOnClickListener {
            Log.d("click", "camera")
            showPopup(profileimageView)
        }

        phonetextView.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        phonetextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //텍스트 변화가 시작될때
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //텍스트에 변화가 있을때
                if(countMatches(s.toString(),"-") < 2){
                    canDonePhone = false
                } else {
                    canDonePhone = true
                }
            }

            override fun afterTextChanged(s: Editable) {
                //텍스트 변화가 끝났을떄
                if(canDoneName&&canDonePhone) {
                    doneButton.isEnabled = true
                } else {
                    doneButton.isEnabled = false
                }
            }
        })

        nametextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //텍스트 변화가 시작될때
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //텍스트에 변화가 있을때
                if(s.toString() != "") {
                    canDoneName = true
                } else {
                    canDoneName = false
                }
            }

            override fun afterTextChanged(s: Editable) {
                //텍스트 변화가 끝났을떄
                if(canDoneName&&canDonePhone) {
                    doneButton.isEnabled = true
                } else {
                    doneButton.isEnabled = false
                }
            }
        })

        doneButton.setOnClickListener{
            val typedName = nametextView.text
            val typedPhone = phonetextView.text
            var typedEmail = emailtextView.text
            val typedInsta = instatextView.text


            if(isEdit==2){
                // 수정된 연락처 저장
                val modifiedContact = ContactData(typedName.toString(), typedPhone.toString(), imageUri, typedEmail.toString(), typedInsta.toString())

                // 연락처 수정
                itemList[index] = modifiedContact

                // 수정된 연락처 저장
                MyApplication.prefs.setContact(itemList)

            }
            else{
                val newContact = ContactData(typedName.toString(), typedPhone.toString(), imageUri, typedEmail.toString(), typedInsta.toString())
                itemList.add(newContact)
                MyApplication.prefs.setContact(itemList)
            }

            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener{
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v) // PopupMenu 객체 선언
        popup.menuInflater.inflate(R.menu.profile_menu, popup.menu) // 메뉴 레이아웃 inflate
        popup.setOnMenuItemClickListener{item->
            when(item.itemId) {
                R.id.action_menu1 -> clickGallery()
                R.id.action_menu2 -> clickCamera()
            }
            true
        }
        popup.show() // 팝업 보여주기
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageUri = saveImageToInternalStorage(bitmap).toString()
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(profileimageView)
        }
    }

    private val activityResult2: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            Glide.with(this).load(imageBitmap).circleCrop().into(profileimageView)
            val uri = saveImageToInternalStorage(imageBitmap)
            imageUri = uri.toString()
            Log.d("Asd",imageUri)
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(applicationContext)
        val long_now = System.currentTimeMillis()
        val imageName = "image"+long_now.toString()+".jpg"
        Log.d("imagename", imageName)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, imageName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun clickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        Log.d("Asd","camera1")
        intent.type = "image/*"
        activityResult.launch(intent)
    }

    private fun clickCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.d("Asd","camera2")

        activityResult2.launch(intent)
    }

    fun countMatches(text: String, search: String): Int {
        var count = 0
        var index = 0

        while (index != -1) {
            index = text.indexOf(search, index)
            if (index != -1) {
                count++
                index += search.length
            }
        }

        return count
    }


}