package com.gadgetsfury.electionindia.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearItemDecoration(private val top: Int, private val bottom: Int, private val left: Int, private val right: Int) : RecyclerView.ItemDecoration() {

    override
    fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.top = top
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right

    }

}