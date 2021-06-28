package com.example.workoutplan

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.adapters.*
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.data.exercise.ExerciseRepository
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.databinding.FragmentExerciseBookBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.EXERCISE_LIMIT
import com.example.workoutplan.utilities.startClickEffect
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.ExerciseBookViewModel
import com.example.workoutplan.viewmodels.factories.ExerciseBookViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.dmoral.toasty.Toasty

class ExerciseBookFragment : Fragment(){

    /**
     * The ViewModel @param {ExerciseBookViewModel}
     */
    private lateinit var viewModel: ExerciseBookViewModel

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentExerciseBookBinding

    /**
     * The adapter for exercises @param {RecyclerView}
     */
    private lateinit var adapterExercises: ExerciseBookAdapter

    /**
     * The adapter for selected exercise @param {RecyclerView}
     */
    private lateinit var adapterExercisesSelected: ExerciseSelectedAdapter

    /**
     * Used to debug
     */
    init {
        Log.d(TAG, "Fragment initialized")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /**
         * //Create a viewModelFactory with the @param {ViewModelProvider} and associate with is Dao
         */
        val viewModelFactory = ExerciseBookViewModelFactory(
                ExerciseRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).ExerciseDao())
                ,ExerciseBookFragmentArgs.fromBundle(requireArguments()).workoutSetup)

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this@ExerciseBookFragment, viewModelFactory).get(
                ExerciseBookViewModel::class.java)

        //Create adapter by the Adapter class for RecyclerView
        // and give it the viewModel already created
        adapterExercises  = ExerciseBookAdapter(
                clickListener = ExerciseListener {
                    onExerciseClickedHandle(it)
                },
                clickAddListener = ExerciseAddListener {
                    onExerciseAddHandle(it)
                }
                ,viewModel = viewModel)

        adapterExercisesSelected = ExerciseSelectedAdapter(
                clickListener = ExerciseSelectedListener {
                    onExerciseSelectedClickedHandle(it)
                }
        )

        //Create the data binding and apply for all the view
        binding = DataBindingUtil.inflate<FragmentExerciseBookBinding>(inflater,
                R.layout.fragment_exercise_book,
                container,
                false
        ).apply {

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@ExerciseBookFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            exerciseBookViewModel = viewModel

            //Set the title
            setTitleToolbar(toolbar)

            workoutList.adapter = adapterExercises

            workoutListSelected.adapter = adapterExercisesSelected

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

            //Set a NavigationUp Listener to the MaterialToolbar
            toolbar.setNavigationOnClickListener { view ->
                viewModel.onReset()
                view.findNavController().navigateUp()
            }
        }

        viewModel.navigateToExercisePage.observe(this.viewLifecycleOwner, { exerciseId ->
            exerciseId?.let {
                val newWorkoutSetup = ExerciseBookFragmentArgs.fromBundle(requireArguments()).workoutSetup.apply {
                    exercises = viewModel.getItemSelectedId()
                }
                this.findNavController().navigate(
                        ExerciseBookFragmentDirections.actionExerciseBookFragmentToExercisePageFragment(newWorkoutSetup, exerciseId))
                viewModel.onExerciseItemNavigated()
            }
        })

        viewModel.exercisesBook.observe(viewLifecycleOwner, {
            it?.let {
                adapterExercises.customSubmitList(it)
                viewModel.isNavigable()
            }
        })

        viewModel.nextButtonEnable.observe(viewLifecycleOwner, {
            binding.nextButton.isEnabled = it
        })

        viewModel.exercisesSelected.observe(this.viewLifecycleOwner, {
            it?.let {
                adapterExercisesSelected.customSubmitList(it)
            }
        })

        return binding.root
    }

    private fun onExerciseSelectedClickedHandle(exercise: Exercise) {
        viewModel.removeFromSelection(exercise)
        setTitleToolbar(binding.toolbar)
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
     */
    private fun onExerciseAddHandle(exercise: Exercise) {
        context?.let {
            if(viewModel.getExerciseSelectedSize() < EXERCISE_LIMIT) {
                viewModel.onAddItem(exercise)
                this.vibratePhone()
                this.startClickEffect(AudioEffectsType.ADD_BUTTON)

            } else {
                Toasty.normal(requireContext(),resources.getString(R.string.exercise_limit_reached), Toast.LENGTH_SHORT).show()
            }
            setTitleToolbar(binding.toolbar)
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