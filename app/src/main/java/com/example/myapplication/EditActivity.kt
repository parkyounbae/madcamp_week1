package com.example.myapplication

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditBinding
import com.example.myapplication.databinding.ActivityResultBinding
import com.example.myapplication.databinding.FragmentBlank3Binding
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

class EditActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBinding.inflate(layoutInflater)}

    private lateinit var dataManager: DataManager
    private lateinit var profileimageView: ImageView
    var imageUri = "@drawable/"+ R.drawable.baseline_person_outline_24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        profileimageView = binding.profileImage
        val nametextView = binding.editName
        val phonetextView = binding.editPhone
        val emailtextView = binding.editEmail
        val instatextView = binding.editInstagram
        val doneButton = binding.contactdoneButton
        val cancelButton = binding.contactcancelButton

        var itemList = mutableListOf<ContactData>()
        itemList = MyApplication.prefs.getContact()

        var isEdit = intent.getSerializableExtra("value") as Int
        var name = intent.getSerializableExtra("contacts") as String
        val index = itemList.indexOfFirst { it.name == name }

        if(isEdit == 2){
            // val resourceId = resources.getIdentifier("@drawable/"+itemList.get(index).imageResId, "drawable", "com.example.myapplication")
            profileimageView.setImageURI(Uri.parse(itemList.get(index).imageResId))
            imageUri = itemList.get(index).imageResId
            nametextView.setText(itemList.get(index).name)
            phonetextView.setText(itemList.get(index).number)
            emailtextView.setText(itemList.get(index).email)
            instatextView.setText(itemList.get(index).instagram)
        }

        profileimageView.setOnClickListener {
            Log.d("click", "camera")
            showPopup(profileimageView)
        }

        doneButton.setOnClickListener{
            val typedName = nametextView.text
            val typedPhone = phonetextView.text
            val typedEmail = emailtextView.text
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
            imageUri = uri.toString()
            Glide.with(this)
                .load(uri)
                .into(profileimageView)
        }
    }

    private val activityResult2: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            profileimageView.setImageBitmap(imageBitmap)
            val uri = saveImageToInternalStorage(imageBitmap)
            imageUri = uri.toString()
            Log.d("Asd","camera")
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "image.jpg")

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


}