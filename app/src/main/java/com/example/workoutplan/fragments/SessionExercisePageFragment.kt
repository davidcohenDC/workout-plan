package com.example.workoutplan.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.databinding.DialogSessionSetBinding
import com.example.workoutplan.databinding.FragmentSessionExercisePageBinding
import com.example.workoutplan.databinding.FragmentWorkoutExercisePageBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.functions.startClickEffect
import com.example.workoutplan.utilities.nameFormat
import com.example.workoutplan.viewmodels.SessionExercisePageViewModel
import com.example.workoutplan.viewmodels.SessionViewModel
import com.example.workoutplan.viewmodels.factories.SessionExercisePageViewModelFactory
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit

class SessionExercisePageFragment :  DialogFragment(){

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by viewModels ({requireParentFragment()})

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSessionExercisePageBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            //Get the layout inflater
            val inflater = requireActivity().layoutInflater

            viewModel.pauseCountDown()

            binding = DataBindingUtil.inflate<FragmentSessionExercisePageBinding>(inflater,
                R.layout.fragment_session_exercise_page,
                null,
                true).apply {
                exercisePageViewModel = viewModel

                executePendingBindings()

                //Set a NavigationUp Listener to the MaterialToolbar
                toolbar.setNavigationOnClickListener { view ->
                    dismiss()
                }



            }
            dialog?.setCanceledOnTouchOutside(true)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onCancel(dialog: DialogInterface) {
        dismiss()
        super.onCancel(dialog)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        const val TAG = "SessionExercisePageFragment"
    }

}
