package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myapplication.databinding.FragmentBlank3Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment3 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        val binding = FragmentBlank3Binding.inflate(layoutInflater)
//
//        Log.d("hhhh","hhhhhhhh")
//
//        binding.startButton.setOnClickListener {
//            Log.d("hhhh2","hhhhhhhh2")
//
//            var imgList = ArrayList<ImageData>()
//            Log.d("hhhh3","hhhhhhhh3")
//
//            imgList.add(ImageData("아이유", R.drawable.image1))
//            imgList.add(ImageData("안유진", R.drawable.image2))
//            imgList.add(ImageData("제니", R.drawable.image3))
//            imgList.add(ImageData("박윤배", R.drawable.image1))
//            imgList.add(ImageData("박성빈", R.drawable.image2))
//            imgList.add(ImageData("하이나리", R.drawable.image6))
//            imgList.add(ImageData("하이리온", R.drawable.image7))
//            imgList.add(ImageData("넙죽이", R.drawable.image8))
//            imgList.add(ImageData("양파쿵야", R.drawable.image9))
//            imgList.add(ImageData("김태희", R.drawable.image10))
//            imgList.add(ImageData("임지연", R.drawable.image11))
//            imgList.add(ImageData("미연", R.drawable.image12))
//            imgList.add(ImageData("카즈하", R.drawable.image13))
//            imgList.add(ImageData("김채원", R.drawable.image14))
//            imgList.add(ImageData("잔망루피", R.drawable.image15))
//            imgList.add(ImageData("쿼카", R.drawable.image16))
//            imgList.add(ImageData("마동석", R.drawable.image17))
//            imgList.add(ImageData("진", R.drawable.image18))
//            imgList.add(ImageData("짱구", R.drawable.image19))
//            imgList.add(ImageData("춘식이", R.drawable.image20))
//            Log.d("hhhh2","hhhhhhhh2")
//
//
//            val intent = Intent(requireContext(), QuizActivity::class.java)
//            intent.putExtra("quizList", imgList)
//            requireContext().startActivity(intent)
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBlank3Binding.inflate(layoutInflater)
        val rootView = binding.root

        Log.d("hhhh","hhhhhhhh")

        binding.startButton.setOnClickListener {
            Log.d("hhhh2","hhhhhhhh2")

            var imgList = ArrayList<ImageData>()
            Log.d("hhhh3","hhhhhhhh3")

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
            Log.d("hhhh2","hhhhhhhh2")


            val intent = Intent(requireContext(), QuizActivity::class.java)
            intent.putExtra("quizList", imgList)
            requireContext().startActivity(intent)
        }


        // return inflater.inflate(R.layout.fragment_blank3, container, false)
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}