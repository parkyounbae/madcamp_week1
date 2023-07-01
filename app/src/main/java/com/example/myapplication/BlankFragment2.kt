package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
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
class BlankFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_blank2, container, false)
        var itemList = getImage()
        val rootView = inflater.inflate(R.layout.fragment_blank2, container, false)
        val gridView: GridView = rootView.findViewById(R.id.gridView)
        val imageAdapter = ImageAdapter(requireContext(), itemList)
        gridView.adapter = imageAdapter

        gridView.setOnItemClickListener { parent, view, position, id ->

            val view = layoutInflater.inflate(R.layout.activity_image_pop_up, null)

            val title = itemList.get(position).name
            val customDialog = CustomDialog(requireContext(), title)

            val builder = AlertDialog.Builder(context).setView(view).create()
//            builder가 팝업되는 화면임

            customDialog.setTitle(itemList.get(position).name)

            val image = customDialog.findViewById<ImageView>(R.id.imagePopup)
            image.setImageResource(itemList.get(position).resId)
            cropImageToSquare(image)
//            val parameter = WindowManager.LayoutParams()
//            parameter.width = WindowManager.LayoutParams.MATCH_PARENT
//            parameter.height = WindowManager.LayoutParams.MATCH_PARENT
            val button = customDialog.findViewById<Button>(R.id.closeButton)
            button.setOnClickListener{
                customDialog.dismiss()
            }
            customDialog.show()


        }


        return rootView
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

    fun getImage() : ArrayList<ImageData> {
        var imgList = ArrayList<ImageData>()

        imgList.add(ImageData("아이유", R.drawable.image1))
        imgList.add(ImageData("안유진", R.drawable.image2))
        imgList.add(ImageData("제니", R.drawable.image3))
        imgList.add(ImageData("박윤배", R.drawable.image1))
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
}