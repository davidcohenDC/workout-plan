package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.MainActivity
import com.example.workoutplan.R
import com.example.workoutplan.adapters.*
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.databinding.FragmentWorkoutExercisesBinding
import com.example.workoutplan.viewmodels.WorkoutExercisesViewModel
import com.example.workoutplan.viewmodels.factories.WorkoutExercisesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.dmoral.toasty.Toasty

class WorkoutExercisesFragment : Fragment(){

    private val viewModel by viewModels<WorkoutExercisesViewModel> {
        WorkoutExercisesViewModelFactory(
                WorkoutRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).workoutDao()),
                WorkoutExerciseCrossRefRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).workoutExerciseCrossRefDao()),
                WorkoutExercisesFragmentArgs.fromBundle(requireArguments()).workoutId
        )
    }

    private lateinit var binding: FragmentWorkoutExercisesBinding

    /**
     * The adapter for exercises @param {RecyclerView}
     */
    private lateinit var adapterExercises: ExerciseDetailedAdapter

    init {
        Log.d(TAG, "Fragment initialized")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

     //   (requireActivity() as MainActivity).hideTitle()

        //Create adapter by the Adapter class for RecyclerView
        // and give it the viewModel already created
        adapterExercises = ExerciseDetailedAdapter(
                clickListener = ExerciseDetailedListener {
                    onExerciseClickedHandle(it)
                },viewModel)

        binding = DataBindingUtil.inflate<FragmentWorkoutExercisesBinding>(inflater,
        R.layout.fragment_workout_exercises,
        container,
        false).apply {
            lifecycleOwner = this@WorkoutExercisesFragment.viewLifecycleOwner

            workoutExerciseList.adapter = adapterExercises

            startButton.setOnClickListener {
                viewModel.onSessionPage()
            }
            (requireActivity() as MainActivity).toogleMenu(toolbar)

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        showDeleteDialog()
                        true
                    }
                    else -> true
                }
            }
        }

        viewModel.exercisesDetailed.observe(viewLifecycleOwner) {
            it?.let {
                adapterExercises.customSubmitList(it)
            }
            binding.executePendingBindings()

        }

        viewModel.selectedWorkout.observe(viewLifecycleOwner) {
            it?.let {
                binding.toolbar.title = it.title
            }
        }

        viewModel.navigateToWorkoutExercisePage.observe(viewLifecycleOwner) {
            it?.let { exerciseId ->
                viewModel.selectedWorkout.value?.let { workoutId ->
                    this.findNavController().navigate(
                        WorkoutExercisesFragmentDirections.actionWorkoutExercisesFragmentToWorkoutExercisePageFragment(
                            exerciseId,
                            workoutId.workoutId
                        )
                    )
                    viewModel.exercisePageDone()
                }
            }
        }

        viewModel.navigateToSessionWorkoutPage.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    WorkoutExercisesFragmentDirections.actionWorkoutExercisesFragmentToSessionFragment2(it)
                )
                viewModel.onSessionPageDone()
            }
        }

        return binding.root

    }

    private fun onExerciseClickedHandle(exercise: ExerciseDetailed) {
        viewModel.onExercisePage(exercise)
    }

    companion object {
        const val TAG = "WorkExercisesFragment"
    }


    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.delete_selection))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.back)) { _, _ ->
                    //NOTHING
                }
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    onMenuItemDelete()
                }
                .show()
    }

    private fun onMenuItemDelete() {
        viewModel.deleteWorkout()
        Toasty.info(requireContext(),resources.getString(R.string.workout_deleted,
                viewModel.selectedWorkout.value?.title), Toasty.LENGTH_SHORT).show()
        this.findNavController().popBackStack()
    }
}


