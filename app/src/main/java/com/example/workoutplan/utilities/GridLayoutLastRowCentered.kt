package com.example.workoutplan.utilities

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun GridLayoutLastRowCentered(
        context: Context,
        spanCount: Int,
        itemsCount: Int,
        @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean,
): GridLayoutManager {

    val lastRowCount = itemsCount % spanCount

    if (lastRowCount == 0) {
        return GridLayoutManager(context, spanCount, orientation, reverseLayout)
    }

    // number of rows with all items
    val fullRows = itemsCount / spanCount
    // "span" counter for whole row (as minimum divider)
    val rowSpan = spanCount * lastRowCount
    // span value for item in the first rows
    val baseRowItemSpan = rowSpan / spanCount
    // span value for item in the last row
    val lastRowItemSpan = rowSpan / lastRowCount

    // return generated manager
    return GridLayoutManager(context, rowSpan, orientation, reverseLayout).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return if (position / spanCount < fullRows) {
                    baseRowItemSpan
                } else {
                    lastRowItemSpan
                }
            }
        }
    }
}