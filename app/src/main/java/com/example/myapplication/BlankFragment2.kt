package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment2 : Fragment(), DataObserver {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var gridView: GridView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var itemList = mutableListOf<ContactData>()
        itemList = MyApplication.prefs.getContact()

        val rootView = inflater.inflate(R.layout.fragment_blank2, container, false)
        // val gridView: GridView = rootView.findViewById(R.id.gridView)
        gridView = rootView.findViewById(R.id.gridView)
        // val imageAdapter = ImageAdapter(requireContext(), itemList)
        imageAdapter = ImageAdapter(requireContext(), itemList)
        gridView.adapter = imageAdapter

        gridView.setOnItemClickListener { parent, view, position, id ->

            val view = layoutInflater.inflate(R.layout.activity_image_pop_up, null)

            val title = itemList.get(position).name
            val customDialog = CustomDialog(requireContext(), title)

            val builder = AlertDialog.Builder(context).setView(view).create()
//            builder가 팝업되는 화면임

            customDialog.setTitle(itemList.get(position).name)

            val image = customDialog.findViewById<ImageView>(R.id.imagePopup)
            // val resourceId = resources.getIdentifier("@drawable/"+itemList.get(position).imageResId, "drawable", "com.example.myapplication")
            image.setImageURI(Uri.parse(itemList.get(position).imageResId))
            cropImageToSquare(image)
            val button = customDialog.findViewById<Button>(R.id.closeButton)
            button.setOnClickListener{
                customDialog.dismiss()
            }
            customDialog.show()
        }


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataManager = (activity as NaviActivity).getDataManager()
        dataManager.registerObserver(this)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDataChanged() {
        var itemList = mutableListOf<ContactData>()
        itemList = MyApplication.prefs.getContact()
        imageAdapter.updateData(itemList)
        imageAdapter.notifyDataSetChanged()
        Log.d("as","asdasda")
    }
}