package com.example.workoutplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.databinding.FragmentSetupWorkoutTitleBinding
import com.example.workoutplan.utilities.hideKeyboard
import com.example.workoutplan.utilities.isAlphabetic
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.SetupWorkoutTitleViewModel
import es.dmoral.toasty.Toasty

class SetupWorkoutTitleFragment : Fragment() {

    private val viewModelSetup: SetupWorkoutTitleViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSetupWorkoutTitleBinding>(
                inflater,
                R.layout.fragment_setup_workout_title,
                container,
                false
        ).apply {

            lifecycleOwner = this.lifecycleOwner
            workoutInputViewModel = viewModelSetup
            workoutInput.setText(viewModelSetup.workoutName)

            workoutInput.doOnTextChanged { text, _, _, count ->
                text?.let {
                    if((count > 0 && it.isAlphabetic())) {
                        viewModelSetup.nextButtonEnable()
                    } else {
                        viewModelSetup.nextButtonDisable()
                        Toasty.warning(requireContext(), resources.getString(R.string.error_only_alphabetic_value), Toast.LENGTH_SHORT).show()
                    }
                    viewModelSetup.setWorkoutName(text.toString())
                }
            }

            workoutInput.setOnEditorActionListener { _, actionId, _ ->
                this@SetupWorkoutTitleFragment.vibratePhone()
                when (actionId) {
                    EditorInfo.IME_ACTION_NEXT -> {
                        if (viewModelSetup.isNextButtonEnabled()) {
                            viewModelSetup.onNavigateNext()
                        } else {
                            Toasty.warning(requireContext(), resources.getString(R.string.error_exercise_name_needed), Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
        }


        viewModelSetup.navigateNext.observe(this.viewLifecycleOwner, {
            it?.let {
                hideKeyboard()
                this.findNavController().navigate(SetupWorkoutTitleFragmentDirections.actionSetupWorkoutTitleFragmentToSetupWorkoutCategoryFragment())
                viewModelSetup.doneNavigating()
            }
        })

        viewModelSetup.nextButtonEnabled.observe(this.viewLifecycleOwner, {
            it?.let {
                binding.nextButton2.isEnabled = it
            }
        })

        return binding.root
    }

    companion object {
        const val TAG = "WorkoutInputFragment"
    }
}