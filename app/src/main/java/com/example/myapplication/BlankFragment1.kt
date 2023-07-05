package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentBlank1Binding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment1 : Fragment() {
    val binding by lazy { FragmentBlank1Binding.inflate(layoutInflater) }
    var contact_DataArray = mutableListOf<ContactData>()
    private lateinit var floatingbutton: FloatingActionButton

    private lateinit var dataManager: DataManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataManager = (activity as NaviActivity).getDataManager()
        dataManager.registerObserver(object : DataObserver{
            override fun onDataChanged() {
                Log.d("as","as")
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 상단바 텍스트 변경
        (activity as AppCompatActivity).supportActionBar?.title = "연락처"

        // 상단바 배경색 변경
//        val color = ContextCompat.getColor(requireContext(), R.color.color1)
//        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
//
//        val textcolor = ContextCompat.getColor(requireContext(), R.color.black)
//        val textColor = ColorUtils.setAlphaComponent(color, 255) // 투명도를 255로 설정
//        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color='$textColor'>새로운 제목</font>"))

        val first = MyApplication.prefs.getBoolean("isFirst", false)
        if (first == false) {
            Log.d("Is first Time?", "first")
            MyApplication.prefs.setBoolean("isFirst", true)
            //앱 최초 실행시 하고 싶은 작업
            contact_DataArray = getContact()
            MyApplication.prefs.setContact(contact_DataArray)
        } else {
            Log.d("Is first Time?", "not first")
            contact_DataArray = MyApplication.prefs.getContact()
        }

        val adapter = ContactAdapter(contact_DataArray)

        binding.rvBoard.adapter = adapter
        binding.rvBoard.layoutManager = LinearLayoutManager(context)

        //divider 구분선 추가
        val itemDecoration = PhDivider(1f, Color.GRAY)
        binding.rvBoard.addItemDecoration(itemDecoration)


        var searchButton = binding.contactSearchEditText

        searchButton.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                // Do Nothing
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do Nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }
        })

//        val fab_view = inflater.inflate(R.layout.fragment_blank1, container, false)
//        floatingbutton = fab_view.findViewById(R.id.floating_button)
        binding.floatingButton.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("value", 1)
            intent.putExtra("contacts", "name")
            startActivity(intent)
        }


        adapter.setItemClickListener(object: ContactAdapter.OnItemClickListener{
            @SuppressLint("ResourceAsColor")
            override fun onClick(v: View, contactData: ContactData) {
                val intent = Intent(context, ContactActivity::class.java)
                intent.putExtra("name", contactData.name)
                Log.d("frag1 name", contactData.name)
                startActivity(intent)
            }
        })

        return binding.root
    }

    fun getContact() : MutableList<ContactData> {
        var contactList = mutableListOf<ContactData>()

        try {
            val json = requireContext().assets.open("contact.json").bufferedReader(Charset.defaultCharset()).use {
                it.readText()
            }

            val jsonArray = JSONArray(json)
            for(i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val number = jsonObject.getString("number")
                val imageName = jsonObject.getString("image")
                val resourceId = resources.getIdentifier("@drawable/"+imageName, "drawable", "com.example.myapplication")
                val uri = "android.resource://${requireContext().packageName}/$resourceId"
                val imageRedId = uri
                val email = jsonObject.getString("email")
                val instagram = jsonObject.getString("instagram")
                val dataItem = ContactData(name, number, imageRedId, email, instagram)
                contactList.add(dataItem)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return contactList

    }

}