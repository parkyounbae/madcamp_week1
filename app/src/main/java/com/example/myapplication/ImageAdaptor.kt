package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ImageAdapter(private var context: Context?, private var imgList: MutableList<ContactData>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
            holder = ViewHolder()
            holder.imageView = view.findViewById(R.id.imageView)
            holder.textView = view.findViewById(R.id.imgName)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val itemData = imgList[position]
        val resourceId = context!!.resources.getIdentifier("@drawable/"+imgList.get(position).imageResId, "drawable", "com.example.myapplication")
        holder.imageView.setImageResource(resourceId)
        cropImageToSquare(holder.imageView)
        holder.textView.text = itemData.name
        holder.imageView.setBackgroundResource(R.drawable.image_corner)
        holder.imageView.clipToOutline = true

        return view!!
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imgList.size
    }

    fun updateData(newData: MutableList<ContactData>) {
        imgList = newData
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

}

private class ViewHolder {
    lateinit var imageView: ImageView
    lateinit var textView: TextView
}
