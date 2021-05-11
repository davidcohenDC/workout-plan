package com.example.workoutplan.utilities

import android.content.Context
import android.media.AudioManager
import androidx.fragment.app.Fragment

/**
 * This function is use to use a default fx effect for all the fragment
 */
fun Fragment.startClickEffect(audioEffectsType: AudioEffectsType) {
    val effect= context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    when(audioEffectsType) {
        AudioEffectsType.ADD_BUTTON -> effect.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR, 1.0f)
        AudioEffectsType.BACK_BUTTON -> effect.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
        AudioEffectsType.ACTION_BUTTON -> effect.playSoundEffect(AudioManager.FX_KEYPRESS_INVALID, 1.0f)
    }
}
