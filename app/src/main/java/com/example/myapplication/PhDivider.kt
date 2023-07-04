package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class PhDivider(private val mHeight: Float, a_color: Int) :
    ItemDecoration() {
    private val mPaint = Paint()

    init {
        mPaint.color = a_color
    }

    override fun onDrawOver(a_canvas: Canvas, a_parent: RecyclerView, a_state: RecyclerView.State) {
        super.onDrawOver(a_canvas, a_parent, a_state)
        val left = a_parent.paddingLeft + 200
        val right = a_parent.width - a_parent.paddingRight -50
        val childCount = a_parent.childCount
        for (i in 0 until childCount) {
            val child = a_parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mHeight
            a_canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom, mPaint)
        }
    }
}