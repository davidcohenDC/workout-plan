package com.example.workoutplan

import android.os.Bundle
import android.util.Log
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
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.databinding.FragmentSetupWorkoutTitleBinding
import com.example.workoutplan.utilities.hideKeyboard
import com.example.workoutplan.utilities.isAlphabetic
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.SetupWorkoutTitleViewModel
import es.dmoral.toasty.Toasty

class SetupWorkoutTitleFragment : Fragment() {

    /**
     * The ViewModel @param {SetupWorkoutTitleViewModel}
     */
    private val viewModel: SetupWorkoutTitleViewModel by viewModels()

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSetupWorkoutTitleBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        Log.d(TAG, "SetupWorkoutTitleFragment created")

        WorkoutPlanDatabase.getInstance(requireNotNull(activity).application).categoryDao()

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentSetupWorkoutTitleBinding>(
                inflater,
                R.layout.fragment_setup_workout_title,
                container,
                false
        ).apply {

            //bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this.lifecycleOwner

            //bind the viewModel data with the actual viewModel
            workoutInputViewModel = viewModel

            //Bind application fot textInputEditText
            workoutInput.apply {

                //Setting the text if exists
                setText(viewModel.workoutName)

                //add listener on text change with controls
                doOnTextChanged { text, _, _, count ->
                    text?.let {
                        if((count > 0 && it.isAlphabetic())) {
                            viewModel.nextButtonEnable()
                        } else {
                            viewModel.nextButtonDisable()
                            Toasty.warning(requireContext(), resources.getString(R.string.error_only_alphabetic_value), Toast.LENGTH_SHORT).show()
                        }
                        viewModel.setWorkoutName(text.toString())
                    }
                }

                //add a editorAction listener
                setOnEditorActionListener { _, actionId, _ ->
                    this@SetupWorkoutTitleFragment.vibratePhone()
                    when (actionId) {
                        EditorInfo.IME_ACTION_NEXT -> {
                            if (viewModel.isNextButtonEnabled()) {
                                viewModel.onNavigateNext()
                            } else {
                                Toasty.warning(requireContext(), resources.getString(R.string.error_exercise_name_needed), Toast.LENGTH_SHORT).show()
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
        }


        //observable live data for the navigation to the next fragment
        viewModel.navigateNext.observe(this.viewLifecycleOwner, {
            it?.let {
                this.hideKeyboard()
                this.findNavController().navigate(SetupWorkoutTitleFragmentDirections.actionSetupWorkoutTitleFragmentToSetupWorkoutCategoryFragment())
                viewModel.doneNavigating()
            }
        })

        //observable live data for enabling the next button
        viewModel.nextButtonStatus.observe(this.viewLifecycleOwner, {
            it?.let {
                binding.nextButton2.isEnabled = it
            }
        })

        return binding.root
    }

    /**
     * Companion Object used to debug
     */
    companion object {
        const val TAG = "SetWorkoutTitleFragment"
    }
}