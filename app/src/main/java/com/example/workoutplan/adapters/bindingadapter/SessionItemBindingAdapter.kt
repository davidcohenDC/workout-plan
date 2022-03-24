package com.example.workoutplan.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.relations.SessionItem

@BindingAdapter("sessionItemImage")
fun ImageView.setSessionItemImage(item: SessionItem?) {
    item?.let {
        setImageResource(
            when (item.status) {
                SessionItem.Companion.STATUS.UNDONE -> R.drawable.ic_box_set
                SessionItem.Companion.STATUS.DOING -> R.drawable.ic_box_set
                SessionItem.Companion.STATUS.DONE -> R.drawable.ic_box_set_full
                SessionItem.Companion.STATUS.PROTO -> R.drawable.ic_borderlessssss
            }
        )
    }
}

@BindingAdapter("sessionItemRepetitionVisible")
fun View.isSessionItemRepetitionVisible(sessionItem: SessionItem?) {
    sessionItem?.let {
        visibility = if(sessionItem.repetition != null) {
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

@BindingAdapter("exerciseSessionImage")
fun ImageView.setExerciseSessionImage(item: Long?) {
    item?.let {
        setImageResource(
            when (it) {
                1L -> R.drawable.ic_child_pose
                2L -> R.drawable.ic_crunch
                3L -> R.drawable.ic_cyclette
                4L -> R.drawable.ic_deadlift
                5L -> R.drawable.ic_hollow_body
                6L -> R.drawable.ic_plank
                7L -> R.drawable.ic_push_up
                8L -> R.drawable.ic_running
                9L -> R.drawable.ic_squat
                10L -> R.drawable.ic_streching
                11L -> R.drawable.ic_jumping_rope
                12L -> R.drawable.ic_curl
                13L -> R.drawable.ic_donkey_kick
                14L -> R.drawable.ic_inclined_curl
                15L -> R.drawable.ic_hoverhead_circles
                16L -> R.drawable.ic_dumbbells_squat
                17L -> R.drawable.ic_pull_up
                18L -> R.drawable.ic_military_press
                19L -> R.drawable.ic_handstand_ball
                20L -> R.drawable.ic_flipping_tire
                21L -> R.drawable.ic_chest_press
                22L -> R.drawable.ic_skip
                23L -> R.drawable.ic_knee_raises
                24L -> R.drawable.ic_bicycle_kicks
                25L -> R.drawable.ic_v_roll_outs
                26L -> R.drawable.ic_v_up
                27L -> R.drawable.ic_cross_body_mountain_clibmbers
                else -> R.drawable.ic_launcher_background
            }
        )
    }
}
