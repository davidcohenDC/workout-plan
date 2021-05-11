//package com.example.workoutplan.adapters
//
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.databinding.BindingAdapter
//import com.example.workoutplan.R
//
//@BindingAdapter("filterImage")
//fun ImageView.setFilterImage(item: FilterItem?) {
//    item?.let {
//        when(item) {
//            is FilterItem.CategoryItem -> {
//                setImageResource(when(item.category.categoryId) {
//                        0 -> R.drawable.ic_baseline_local_fire
//                        1 -> R.drawable.ic_drop
//                        2 -> R.drawable.ic_baseline_health_and_safety_24
//                        else -> R.drawable.ic_launcher_foreground
//                    }
//                )
//            }
//            is FilterItem.DifficultyItem -> {
//                setImageResource(when(item.difficulty.difficultyId) {
//                    0 -> R.drawable.ic_beginner
//                    1 -> R.drawable.ic_average
//                    2 -> R.drawable.ic_expert
//                    4 -> R.drawable.ic_advance
//                    else -> R.drawable.ic_launcher_foreground
//                }
//                )
//            }
//        }
//    }
//}
//
//@BindingAdapter("filterText")
//fun TextView.setFilterTExt(item: FilterItem?) {
//    item?.let {
//        when(item) {
//            is FilterItem.CategoryItem -> {
//                text = resources.getString(when(item.category.categoryId) {
//                        0 -> R.string.strength
//                        1 -> R.string.cardio
//                        2 -> R.string.healthy
//                        else -> R.string.app_name
//                    }
//                )
//            }
//            is FilterItem.DifficultyItem -> {
//                text = resources.getString(when(item.difficulty.difficultyId) {
//                    0 -> R.string.beginner
//                    1 -> R.string.intermediate
//                    2 -> R.string.expert
//                    3 -> R.string.expert
//                    else -> R.string.app_name
//                }
//                )
//            }
//        }
//    }
//}