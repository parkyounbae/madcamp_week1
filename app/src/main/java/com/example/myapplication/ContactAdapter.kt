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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contact = contactList.get(position)
        holder.setContact(contact)
    }


}

class Holder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setContact(contact: ContactData) {
        binding.name.text = contact.name
        binding.number.text = contact.number
    }
}