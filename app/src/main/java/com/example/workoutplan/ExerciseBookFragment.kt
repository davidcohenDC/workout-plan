package com.example.workoutplan

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.adapters.ExerciseAddListener
import com.example.workoutplan.adapters.ExerciseBookAdapter
import com.example.workoutplan.adapters.ExerciseListener
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.data.exercise.ExerciseRepository
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.databinding.FragmentExerciseBookBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.EXERCISE_LIMIT
import com.example.workoutplan.utilities.startClickEffect
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.ExerciseBookViewModel
import com.example.workoutplan.viewmodels.ExerciseBookViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.dmoral.toasty.Toasty

class ExerciseBookFragment : Fragment(){

    private lateinit var viewModel: ExerciseBookViewModel

    private lateinit var binding: FragmentExerciseBookBinding

    private lateinit var adapter: ExerciseBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate<FragmentExerciseBookBinding>(inflater,
                R.layout.fragment_exercise_book,
                container,
                false
        ).apply {
            lifecycleOwner = this@ExerciseBookFragment.viewLifecycleOwner

            val viewModelFactory = ExerciseBookViewModelFactory(
                ExerciseRepository(
                    dao = WorkoutPlanDatabase.getInstance(
                            requireNotNull(activity).application
                    ).ExerciseDao())
            )

            viewModel = ViewModelProvider(this@ExerciseBookFragment, viewModelFactory).get(
                    ExerciseBookViewModel::class.java)

            exerciseBookViewModel = viewModel

            adapter  = ExerciseBookAdapter(
                    clickListener = ExerciseListener {
                        onExerciseClickedHandle(it)
                    },
                    clickAddListener = ExerciseAddListener {
                        onExerciseAddHandle(it, toolbar)
                    }
            )
            workoutList.adapter = adapter

            toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.action_restore -> {
                        if(viewModel.getExerciseSelectedSize() > 0) {
                            showRestoreDialog()
                        }
                        true
                    }
                    else -> false
                }
            }

            toolbar.setNavigationOnClickListener { view ->
                viewModel.onReset()
                view.findNavController().navigateUp()

            }

            setTitleToolbar(toolbar)
        }

        viewModel.navigateToExercisePage.observe(this.viewLifecycleOwner, { exerciseId ->
            exerciseId?.let {
                this.findNavController().navigate(
                        ExerciseBookFragmentDirections.actionExerciseBookFragmentToExercisePageFragment(exerciseId))
                viewModel.onExerciseItemNavigated()
            }
        })

        viewModel.exercisesBook.observe(viewLifecycleOwner, {
            it?.let {
                adapter.customSubmitList(it)
                viewModel.isNavigable()
            }
        })

        viewModel.nextButtonEnable.observe(viewLifecycleOwner, {
            binding.nextButton.isEnabled = it
        })

        return binding.root
    }

    /**
     * Restore the selection and the exercises and set the Title toolbar
     */
    private fun onMenuItemRestore() {
        viewModel.onReset()
        setTitleToolbar(binding.toolbar)
        this.startClickEffect(AudioEffectsType.ACTION_BUTTON)
        this.vibratePhone()
    }

    /**
     * Pass the exercise id to viewModel, after the observer will navigate to the Page Fragment
     * @param exercise
     */
    private fun onExerciseClickedHandle(exercise: Exercise) {
        viewModel.onExerciseItemClicked(exercise.exerciseId)
    }

    /**
     * Handle the Add exercise to selected items
     * and set the TitleToolbar
     * @param exercise
     * @param toolbar
     */
    private fun onExerciseAddHandle(exercise: Exercise, toolbar: MaterialToolbar) {
        context?.let {
            if(viewModel.getExerciseSelectedSize() < EXERCISE_LIMIT) {
                viewModel.onAddItem(exercise)
                this.vibratePhone()
                this.startClickEffect(AudioEffectsType.ADD_BUTTON)
            } else {
                Toasty.normal(requireContext(),resources.getString(R.string.exercise_limit_reached), Toast.LENGTH_SHORT).show()
            }
            setTitleToolbar(toolbar)
        }
    }

    /**
     * Show the restore dialog after press MenuItem restore
     */
    private fun showRestoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.reset_selection))
                .setMessage(getString(R.string.reset_selection_description))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.back)) { _, _ ->
                    //NOTHING
                }
                .setPositiveButton(getString(R.string.confirm)) {_,_ ->
                    onMenuItemRestore()
                }
                .show()
    }

    /**
     * Util function for change the title text toolbar with the actual selection
     * @param toolbar
     */
    private fun setTitleToolbar(toolbar: MaterialToolbar) {
        if(viewModel.getExerciseSelectedSize() > 0) {
            toolbar.setBackgroundColor(Color.DKGRAY)
            toolbar.title = "( ${viewModel.getExerciseSelectedSize()} / $EXERCISE_LIMIT " +" )"
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            toolbar.title = resources.getString(R.string.exercise_book)
        }
    }

    companion object {
        const val TAG = "ExerciseBookFragment"
    }
}