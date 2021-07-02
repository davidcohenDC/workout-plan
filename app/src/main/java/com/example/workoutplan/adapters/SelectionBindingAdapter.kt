package com.example.workoutplan.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.google.android.material.card.MaterialCardView

@BindingAdapter("isChecked")
fun MaterialCardView.isCategoryChecked(checked: Boolean) {
    checked.let {
        isChecked = checked
    }
}

@BindingAdapter("selectionImage")
fun ImageView.setCategoryImage(selection: SelectionItem?) {
    when (selection) {
        is SelectionItem.CategoryItem -> setImageResource(when (selection.id) {
            1L -> R.drawable.ic_baseline_local_fire
            2L -> R.drawable.ic_drop
            3L -> R.drawable.ic_baseline_health_and_safety_24
            else -> R.drawable.ic_launcher_foreground
        })
        is SelectionItem.DifficultyItem -> setImageResource(when (selection.id) {
            1L -> R.drawable.ic_beginner
            2L -> R.drawable.ic_average
            3L -> R.drawable.ic_advance
            4L -> R.drawable.ic_expert
            else -> R.drawable.ic_launcher_background
        })
        is SelectionItem.MuscleItem -> setImageResource(when (selection.id) {
            1L -> R.drawable.ic_chest
            2L -> R.drawable.ic_arms
            3L -> R.drawable.ic_back
            4L -> R.drawable.ic_abs
            5L -> R.drawable.ic_leg
            else -> R.drawable.ic_launcher_background
        })
    }

}

@BindingAdapter("selectionText")
fun TextView.setCategoryText(selection: SelectionItem?) {
    selection?.let {
        when (selection) {
            is SelectionItem.CategoryItem -> {
                text = resources.getString(when (selection.id) {
                    1L -> R.string.strength
                    2L -> R.string.cardio
                    3L -> R.string.healthy
                    else -> R.string.app_name
                })
            }
            is SelectionItem.DifficultyItem -> text = resources.getString(when (selection.id) {
                1L -> R.string.beginner
                2L -> R.string.intermediate
                3L -> R.string.advance
                4L -> R.string.expert
                else -> R.string.app_name
            })
            is SelectionItem.MuscleItem -> {
                text = resources.getString(when (selection.id) {
                    1L -> R.string.chest
                    2L -> R.string.arms
                    3L -> R.string.muscle_back
                    4L -> R.string.abdominal
                    5L -> R.string.leg
                    else -> R.string.app_name
                })
            }
        }
    }
}