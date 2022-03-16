package com.example.workoutplan.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.adapters.items.SessionItem

@BindingAdapter("sessionItemImage")
fun ImageView.setSessionItemImage(item: SessionItem?) {
    item?.let {
        setImageResource(
            when (item.status) {
                SessionItem.Companion.STATUS.UNDONE -> R.drawable.ic_box_set
                SessionItem.Companion.STATUS.DOING -> R.drawable.ic_box_set
                SessionItem.Companion.STATUS.DONE -> R.drawable.ic_box_set_full
            }
        )
    }
}

@BindingAdapter("sessionItemRepetitionVisible")
fun View.isSessionItemRepetitionVisible(sessionItem: SessionItem?) {
    sessionItem?.let {
        visibility = if(sessionItem.duration != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

@BindingAdapter("sessionItemWeightVisible")
fun View.isSessionItemWeightVisible(sessionItem: SessionItem?) {
    sessionItem?.let {
        visibility = if(sessionItem.weight != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

@BindingAdapter("sessionItemDurationVisible")
fun View.isSessionItemDurationVisible(sessionItem: SessionItem?) {
    sessionItem?.let {
        visibility = if(sessionItem.duration != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
