package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
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
import java.util.Locale
import java.util.concurrent.TimeUnit

class EditActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBinding.inflate(layoutInflater)}

    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val profileimageView = binding.profileImage
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
            val resourceId = resources.getIdentifier("@drawable/"+itemList.get(index).imageResId, "drawable", "com.example.myapplication")
            profileimageView.setImageResource(resourceId)
            nametextView.setText(itemList.get(index).name)
            phonetextView.setText(itemList.get(index).number)
            emailtextView.setText(itemList.get(index).email)
            instatextView.setText(itemList.get(index).instagram)
        }

        doneButton.setOnClickListener{
            val typedName = nametextView.text
            val typedPhone = phonetextView.text
            val typedEmail = emailtextView.text
            val typedInsta = instatextView.text


            if(isEdit==2){
                // 수정된 연락처 저장
                val modifiedContact = ContactData(typedName.toString(), typedPhone.toString(), "image1", typedEmail.toString(), typedInsta.toString())

                // 연락처 수정
                itemList[index] = modifiedContact

                // 수정된 연락처 저장
                MyApplication.prefs.setContact(itemList)

//            val newContact = ContactData(typedName.toString(), typedPhone.toString(), "image1", typedEmail.toString(), typedInsta.toString())
//            val contact_list = MyApplication.prefs.getContact()
//            contact_list.add(newContact)
//            MyApplication.prefs.setContact(contact_list)
            }
            else{
                val newContact = ContactData(typedName.toString(), typedPhone.toString(), "image1", typedEmail.toString(), typedInsta.toString())
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

}