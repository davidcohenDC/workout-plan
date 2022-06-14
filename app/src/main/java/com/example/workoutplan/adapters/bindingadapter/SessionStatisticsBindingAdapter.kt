package com.example.workoutplan.adapters.bindingadapter

import android.text.format.DateUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.data.entity.Session
import com.google.android.material.textview.MaterialTextView
import java.util.concurrent.TimeUnit

@BindingAdapter("sessionRatingImage")
fun ImageView.setSessionRatingImage(item: Session?) {
    item?.let {
        setImageResource(
            when (item.rating) {
                1 -> R.drawable.ic_rating_1
                2 -> R.drawable.ic_rating_2
                3 -> R.drawable.ic_rating_3
                4 -> R.drawable.ic_rating_4
                5 -> R.drawable.ic_rating_5
                else -> R.color.trans
            }
        )
    }
}

@BindingAdapter("sessionCompletedText")
fun MaterialTextView.setSessionCompletedText(item: Session?) {
    item?.let {
        it.completed?.let { comp ->
            text = if(comp!=0.0) {
                val txt = "${String.format("%.2f", it.completed,R.string.session_completed)}% completato"
                txt
            } else {
                "0% completato"
            }
        }

    }
}

@BindingAdapter("sessionDurationText")
fun MaterialTextView.setSessionDurationText(item: Session?) {
    item?.let {
        val date = "${TimeUnit.MILLISECONDS.toMinutes(it.duration)} min"
        text = date
    }
}

@BindingAdapter("sessionRepText")
fun MaterialTextView.setSessionRepetitionText(item: Session?) {
    item?.let {
        text = String.format(resources.getString(R.string.repetition_data),it.totalRepetitions)
    }
}

@BindingAdapter("sessionSetText")
fun MaterialTextView.setSessionSetText(item: Session?) {
    item?.let {
        text = String.format(resources.getString(R.string.set_data),it.totalSets)
    }
}

