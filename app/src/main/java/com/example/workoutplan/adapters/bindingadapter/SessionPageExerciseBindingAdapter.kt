package com.example.workoutplan.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.google.android.material.appbar.CollapsingToolbarLayout


@BindingAdapter("sessionExerciseImagePage")
fun ImageView.setSessionExerciseImagePage(item: Long?) {
    item?.let {
        setImageResource(
            when (item) {
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

@BindingAdapter("sessionExerciseTitlePage")
fun TextView.setSessionExerciseTitlePage(item: Long?) {
    item?.let {
        text = resources.getString(when (item) {
            1L -> R.string.child_pose
            2L -> R.string.crunch
            3L -> R.string.exercise_bike
            4L -> R.string.deadlift
            5L -> R.string.hollow_body
            6L -> R.string.plank
            7L -> R.string.push_up
            8L -> R.string.jogging
            9L -> R.string.squat
            10L -> R.string.stretching
            11L -> R.string.jumping_rope
            12L -> R.string.curl
            13L -> R.string.donkey_kick
            14L -> R.string.inclined_curl
            15L -> R.string.hoverhead_circles
            16L -> R.string.dumbbells_curl
            17L -> R.string.pull_up
            18L -> R.string.miitary_press
            19L -> R.string.ball_handstand
            20L -> R.string.flipping_tire
            21L -> R.string.bench_press
            22L -> R.string.v_up
            23L -> R.string.knee_raises
            24L -> R.string.bicycle_kicks
            25L -> R.string.skip
            26L -> R.string.roll_out
            27L -> R.string.cross_mountain_climbers
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseTitlePage")
fun CollapsingToolbarLayout.setSessionExerciseTitlePage(item: Long?) {
    item?.let {
        title = resources.getString(when (item) {
            1L -> R.string.child_pose
            2L -> R.string.crunch
            3L -> R.string.exercise_bike
            4L -> R.string.deadlift
            5L -> R.string.hollow_body
            6L -> R.string.plank
            7L -> R.string.push_up
            8L -> R.string.jogging
            9L -> R.string.squat
            10L -> R.string.stretching
            11L -> R.string.jumping_rope
            12L -> R.string.curl
            13L -> R.string.donkey_kick
            14L -> R.string.inclined_curl
            15L -> R.string.hoverhead_circles
            16L -> R.string.dumbbells_curl
            17L -> R.string.pull_up
            18L -> R.string.miitary_press
            19L -> R.string.ball_handstand
            20L -> R.string.flipping_tire
            21L -> R.string.bench_press
            22L -> R.string.v_up
            23L -> R.string.knee_raises
            24L -> R.string.bicycle_kicks
            25L -> R.string.skip
            26L -> R.string.roll_out
            27L -> R.string.cross_mountain_climbers
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseDescriptionPage")
fun TextView.setSessionExerciseDescriptionPage(item: Long?) {
    item?.let {
        text = resources.getString(when (item) {
            1L -> R.string.child_pose_description
            2L -> R.string.crunch_description
            3L -> R.string.exercise_bike_description
            4L -> R.string.deadlift_description
            5L -> R.string.hollow_body_description
            6L -> R.string.plank_description
            7L -> R.string.push_up_description
            8L -> R.string.jogging_description
            9L -> R.string.squat_description
            10L -> R.string.stretching_description
            11L -> R.string.jumping_rope_description
            12L -> R.string.curl_description
            13L -> R.string.donkey_kick_description
            14L -> R.string.inclined_curl_description
            15L -> R.string.hoverhead_circles_description
            16L -> R.string.dumbbells_curl_description
            17L -> R.string.pull_up_description
            18L -> R.string.military_press_description
            19L -> R.string.ball_handstand_description
            20L -> R.string.flipping_tire_description
            21L -> R.string.bench_press_description
            else ->  R.string.stretching_description
        }
        )
    }
}

@BindingAdapter("sessionExerciseMuscleGroupPage")
fun TextView.setSessionExerciseMuscleGroupPage(item: Long?) {
    item?.let {
        text = resources.getString(when (item) {
            1L -> R.string.chest
            2L -> R.string.arms
            3L -> R.string.back
            4L -> R.string.abdominal
            5L -> R.string.leg
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseMuscleGroupImagePage")
fun ImageView.setSessionExerciseMuscleGroupImagePage(item: Long?) {
    item?.let {
        setImageResource(when (item) {
            1L -> R.drawable.ic_chest
            2L -> R.drawable.ic_arms
            3L -> R.drawable.ic_back
            4L -> R.drawable.ic_abs
            5L -> R.drawable.ic_leg
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseDifficultyPage")
fun TextView.setSessionExerciseDifficultyPage(item: Long?) {
    item?.let {
        text = resources.getString(when (item) {
            1L -> R.string.beginner
            2L -> R.string.intermediate
            3L -> R.string.advance
            4L -> R.string.expert
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseDifficultyImagePage")
fun ImageView.setSessionExerciseDifficultyImagePage(item: Long?) {
    item?.let {
        setImageResource(when (item) {
            1L -> R.drawable.ic_beginner
            2L -> R.drawable.ic_average
            3L -> R.drawable.ic_advance
            4L -> R.drawable.ic_expert
            else -> R.drawable.ic_launcher_background
        }
        )
    }
}

@BindingAdapter("sessionBadgeViewPage")
fun ImageView.setSessionBadgeViewPage(item: Long?) {
    item?.let {
        when (item) {
            1L -> {
                this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_local_fire))
                this.visibility = View.VISIBLE
            }
            2L -> {
                this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_drop))
                this.visibility = View.VISIBLE
            }
            3L -> {
                this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_health_and_safety_24))
                this.visibility = View.VISIBLE
            }
            else -> this.visibility = View.GONE
        }
    }
}

@BindingAdapter("sessionExerciseCategoryImagePage")
fun ImageView.setSessionExerciseCategoryImagePage(item: Long?) {
    item?.let {
        setImageResource(when (item) {
            1L -> R.drawable.ic_baseline_local_fire
            2L -> R.drawable.ic_drop
            3L -> R.drawable.ic_baseline_health_and_safety_24
            else -> R.drawable.ic_launcher_background
        }
        )
    }
}

@BindingAdapter("sessionExerciseCategoryTextPage")
fun TextView.setSessionExerciseCategoryTextPage(item: Long?) {
    item?.let {
        text = resources.getString(when (item) {
            1L -> R.string.strength
            2L -> R.string.cardio
            3L -> R.string.healthy
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("sessionExerciseDetailedImage")
fun ImageView.setSessionExerciseDetailedImage(item: Long?) {
    item?.let {
        setImageResource(
            when (item) {
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




