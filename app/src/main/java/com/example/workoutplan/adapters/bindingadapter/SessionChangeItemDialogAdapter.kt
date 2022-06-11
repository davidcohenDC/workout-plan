package com.example.workoutplan.adapters.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.workoutplan.data.relations.SessionItem
import com.shawnlin.numberpicker.NumberPicker
import java.util.concurrent.TimeUnit

class SessionChangeItemDialogAdapter {

    @BindingAdapter("repetitionDialogVisible")
    fun View.isRepetitionDialogVisible(sessionItem: SessionItem?) {
        sessionItem?.let {
            visibility = when (it.repetition) {
                null -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    @BindingAdapter("durationDialogVisible")
    fun View.isDurationDialogVisible(sessionItem: SessionItem?) {
        sessionItem?.let {
            visibility = when(sessionItem.duration) {
                null -> View.GONE
                else -> View.VISIBLE
            }
        }

    }

    @BindingAdapter("weightDialogVisible")
    fun View.isWeightDialogVisible(sessionItem: SessionItem?) {
        sessionItem?.let {
            visibility = when(it.weight) {
                null -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    @BindingAdapter("getMinuteFromSecond")
    fun NumberPicker.getMinuteFromSecond(second: Long?) {
        value =  second?.let { TimeUnit.SECONDS.toMinutes(it).toInt() } ?: 0
    }
}