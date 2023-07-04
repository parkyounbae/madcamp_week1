package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
                val view = layoutInflater.inflate(R.layout.contact_dialog, null)
                val detailContactDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog).setView(view).create()


                val detailName = view.findViewById<TextView>(R.id.detailContactName)
                val detailImage = view.findViewById<ImageView>(R.id.detailContactImageView)
                val detailNumber = view.findViewById<TextView>(R.id.detailContactNumber)

                val detailEmail = view.findViewById<TextView>(R.id.detailContactEmail)
                val detailInstagram = view.findViewById<TextView>(R.id.detailContactInstagram)
                val detailClose = view.findViewById<Button>(R.id.detailContactClose)
                val detailRemove = view.findViewById<Button>(R.id.contactRemove)
                val detailEdit = view.findViewById<Button>(R.id.editButton)

                detailName.text = contactData.name

                // 이미지의 리소스 식별자를 가져오기
                // val resourceId = resources.getIdentifier("@drawable/"+contactData.imageResId, "drawable", "com.example.myapplication")

                detailImage.setImageURI(Uri.parse(contactData.imageResId))
                detailNumber.text = contactData.number
                if(contactData.email == "") {
                    detailEmail.text = "Email"
                    detailEmail.setTextColor(R.color.gray)
                } else {
                    detailEmail.text = contactData.email
                }

                if(contactData.instagram == "") {
                    detailInstagram.text = "Instagram"
                    detailInstagram.setTextColor(R.color.gray)
                } else {
                    detailInstagram.text = contactData.instagram
                }

                //연락처 삭제 코드
                detailRemove.setOnClickListener {
                    val dialog = AlertDialog.Builder(context)
//                        .setTitle("연락처를 삭제하시겠습니까?")
                        .setMessage("연락처를 삭제하시겠습니까?")
                        .setPositiveButton("확인") { _, _ ->
                            contact_DataArray.removeAt(contact_DataArray.indexOf(contactData))
                            MyApplication.prefs.setContact(contact_DataArray)
                            adapter.notifyDataSetChanged()
                            dataManager.setData("new")
                            detailContactDialog.dismiss()
                            val intent = Intent(context, NaviActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { dialog , _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()

//                    contact_DataArray.removeAt(contact_DataArray.indexOf(contactData))
//                    MyApplication.prefs.setContact(contact_DataArray)
//                    adapter.notifyDataSetChanged()
//                    dataManager.setData("new")
//                    detailContactDialog.dismiss()
                }

                detailClose.setOnClickListener {
                    detailContactDialog.dismiss()
                }

                detailEdit.setOnClickListener {
                    val intent = Intent(context, EditActivity::class.java)
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
                        Toast.makeText(context, "Failed to open email client", Toast.LENGTH_SHORT).show()
                    }
                }



                detailContactDialog.show()
                detailContactDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

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