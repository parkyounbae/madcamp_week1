package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
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
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentBlank1Binding
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

        var itemList = getImage()

        val adapter = ContactAdapter(contact_DataArray)

        binding.rvBoard.adapter = adapter
        binding.rvBoard.layoutManager = LinearLayoutManager(context)


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

        adapter.setItemClickListener(object: ContactAdapter.OnItemClickListener{
            override fun onClick(v: View, contactData: ContactData) {
                val view = layoutInflater.inflate(R.layout.contact_dialog, null)
                val detailContactDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog).setView(view).create()


                val detailName = view.findViewById<TextView>(R.id.detailContactName)
                val detailImage = view.findViewById<ImageView>(R.id.detailContactImageView)
                val detailNumber = view.findViewById<TextView>(R.id.detailContactNumber)

                val detailEmail = view.findViewById<TextView>(R.id.detailContactEmail)
                val detailInstagram = view.findViewById<TextView>(R.id.detailContactInstagram)
                val detailClose = view.findViewById<Button>(R.id.detailContactClose)

                detailName.text = contactData.name

                // 이미지의 리소스 식별자를 가져오기
                val resourceId = resources.getIdentifier("@drawable/"+contactData.imageResId, "drawable", "com.example.myapplication")

                detailImage.setImageResource(resourceId)
                detailNumber.text = contactData.number
                detailEmail.text = contactData.email
                detailInstagram.text = contactData.instagram

                detailClose.setOnClickListener {
                    contact_DataArray.removeAt(contact_DataArray.indexOf(contactData))
                    MyApplication.prefs.setContact(contact_DataArray)
                    adapter.notifyDataSetChanged()

                    detailContactDialog.dismiss()
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

    fun getImage() : ArrayList<ImageData> {
        var imgList = ArrayList<ImageData>()

        imgList.add(ImageData("아이유", R.drawable.image1))
        imgList.add(ImageData("안유진", R.drawable.image2))
        imgList.add(ImageData("제니", R.drawable.image3))
        imgList.add(ImageData("박윤배", R.drawable.image4))
        imgList.add(ImageData("박성빈", R.drawable.image2))
        imgList.add(ImageData("하이나리", R.drawable.image6))
        imgList.add(ImageData("하이리온", R.drawable.image7))
        imgList.add(ImageData("넙죽이", R.drawable.image8))
        imgList.add(ImageData("양파쿵야", R.drawable.image9))
        imgList.add(ImageData("김태희", R.drawable.image10))
        imgList.add(ImageData("임지연", R.drawable.image11))
        imgList.add(ImageData("미연", R.drawable.image12))
        imgList.add(ImageData("카즈하", R.drawable.image13))
        imgList.add(ImageData("김채원", R.drawable.image14))
        imgList.add(ImageData("잔망루피", R.drawable.image15))
        imgList.add(ImageData("쿼카", R.drawable.image16))
        imgList.add(ImageData("마동석", R.drawable.image17))
        imgList.add(ImageData("진", R.drawable.image18))
        imgList.add(ImageData("짱구", R.drawable.image19))
        imgList.add(ImageData("춘식이", R.drawable.image20))
        return imgList
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
                val imageRedId = jsonObject.getString("image")
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