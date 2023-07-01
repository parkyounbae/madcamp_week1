package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ContactItemBinding

class ContactAdapter: RecyclerView.Adapter<Holder>() {
    var contactList = mutableListOf<ContactData>()
    private lateinit var binding: ContactItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contact = contactList.get(position)
        holder.setContact(contact)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


}

class Holder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setContact(contact: ContactData) {
        binding.name.text = contact.name
        binding.number.text = contact.number
    }
}