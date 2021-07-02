package com.example.workoutplan.utilities

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.workoutplan.R
import com.example.workoutplan.data.entity.Exercise
import kotlin.math.abs

fun CharSequence.isAlphabetic(): Boolean {
    return all { it.isLetter() || it.isWhitespace() }
}

fun Fragment.hideKeyboard2() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.nameFormat(item: Exercise?): String {
    var name = ""
    item?.let {
        name = resources.getString(when (item.exerciseId) {
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
            else -> R.string.app_name
        }
        )
    }
    return name
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = requireActivity().currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun selectionCompositeTransformer(): CompositePageTransformer {
    return CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(40))
        addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
    }
}

fun Fragment.addTouch3D() {
    this.vibratePhone()
    this.startClickEffect(AudioEffectsType.ADD_BUTTON)
}


fun Fragment.getDrawable(drawable: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), drawable)
}
