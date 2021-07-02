package com.example.workoutplan.utilities

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SelectionItemDecoration(
        val spanCount: Int,
        val spanSize: Int,
        val spacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        val width = parent.width
        val childWidth = (width / spanSize) - spacing
        val tmp = (width - spacing * (spanCount - 1) - childWidth * spanCount) / 2

        if (spanCount == 1) {
            outRect.left = tmp
            outRect.right = tmp
        } else {
            when (column) {
                0 -> {
                    outRect.left = tmp
                    outRect.right = spacing
                }
                (spanCount - 1) -> {
                    outRect.left = spacing
                    outRect.right = tmp
                }
                else -> {
                    outRect.left = spacing / 2
                    outRect.right = spacing / 2
                }
            }
        }

        if (position >= spanCount) {
            outRect.top = spacing / 2
        }
    }
}