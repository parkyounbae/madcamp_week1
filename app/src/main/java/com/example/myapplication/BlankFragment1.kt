package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ContactItemBinding
import com.example.myapplication.databinding.FragmentBlank1Binding
import com.google.android.gms.wearable.DataItem
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
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    // lateinit var binding : FragmentBlank1Binding
//
//    val contact_DataArray : ArrayList<ContactData> = ArrayList()
//    lateinit var recyclerView : RecyclerView

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    val binding by lazy { FragmentBlank1Binding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var contact_DataArray = getContact()

        val adapter = ContactAdapter()
        adapter.contactList = contact_DataArray
        binding.rvBoard.adapter = adapter
        binding.rvBoard.layoutManager = LinearLayoutManager(context)
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
                val dataItem = ContactData(name, number)
                contactList.add(dataItem)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return contactList

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BlankFragment1.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BlankFragment1().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}