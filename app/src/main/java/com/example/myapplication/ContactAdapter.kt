package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color.rgb
import android.graphics.ColorSpace.Rgb
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ContactItemBinding
import java.util.Random
import kotlin.coroutines.coroutineContext

class ContactAdapter(val contactList: MutableList<ContactData>): RecyclerView.Adapter<Holder>(), Filterable {
    private lateinit var binding: ContactItemBinding
    private var files: MutableList<ContactData>? = contactList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return files?.size!!
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, contactData: ContactData)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contact = files?.get(position)
        if (contact != null) {
            holder.setContact(contact)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, files!!.get(position))
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                files = if (charString.isEmpty()) {
                    contactList
                } else {
                    val filteredList = ArrayList<ContactData>()
                    if (contactList != null) {
                        for (tempContact in contactList) {
                            if(tempContact.name.contains(charString)) {
                                filteredList.add(tempContact);
                                Log.d("name", tempContact.name)
                            }
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = files
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                files  = results.values as MutableList<ContactData>
                notifyDataSetChanged()
            }
        }
    }
}

class Holder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ResourceAsColor")
    fun setContact(contact: ContactData) {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.setColor(getRandomColor()) // 배경 색상을 원하는 색상으로 변경합니다

        val radius = binding.name.width / 2.toFloat()
        shapeDrawable.cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)

        binding.name.background = shapeDrawable
        binding.name.text = contact.name.get(0).toString()
        binding.number.text = contact.name
    }

    fun getRandomColor():Int{
        var colors = ArrayList<Int>()
        colors.add(rgb(242,166,139))
        colors.add(rgb(136,193,187))
        colors.add(rgb(156,165,237))
        colors.add(rgb(143,199,246))
        colors.add(rgb(204,216,133))
        colors.add(rgb(143,199,246))

        val random = Random()
        return colors.get(random.nextInt(6))
    }
}