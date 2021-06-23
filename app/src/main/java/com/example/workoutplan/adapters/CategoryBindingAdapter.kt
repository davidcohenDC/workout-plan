package com.example.workoutplan.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.data.category.Category
import com.google.android.material.card.MaterialCardView
import java.util.*

@BindingAdapter("isChecked")
fun MaterialCardView.isCategoryChecked(checked: Boolean) {
    checked.let {
        isChecked = checked
    }
}

@BindingAdapter("categoryImage")
fun ImageView.setCategoryImage(category: Category?) {
    category?.let {
        setImageResource(when(category.categoryId) {
            1L -> R.drawable.ic_baseline_local_fire
            2L -> R.drawable.ic_drop
            3L -> R.drawable.ic_baseline_health_and_safety_24
            else -> R.drawable.ic_launcher_foreground
        }
        )
    }
}

@BindingAdapter("categoryText")
fun TextView.setCategoryText(category: Category?) {
    category?.let {
        text =  resources.getString(when(category.categoryId) {
            1L -> R.string.strength
            2L -> R.string.cardio
            3L -> R.string.healthy
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("categoryDescription")
fun TextView.setCategoryDescription(category: Category?) {
    category?.let {
        text =  resources.getString(when(category.categoryId) {
            1L -> R.string.strength_description
            2L -> R.string.cardio_description
            3L -> R.string.healthy_description
            else -> R.string.app_name
        }
        )
    }
}