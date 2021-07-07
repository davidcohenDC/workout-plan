package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.workoutplan.R
import com.example.workoutplan.adapters.SelectionAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.MuscleRepository
import com.example.workoutplan.databinding.FragmentSetupWorkoutSelectionsBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.functions.startClickEffect
import com.example.workoutplan.utilities.functions.vibratePhone
import com.example.workoutplan.utilities.selectionCompositeTransformer
import com.example.workoutplan.viewmodels.SetupWorkoutMuscleViewModel
import com.example.workoutplan.viewmodels.factories.SetupWorkoutMuscleViewModelFactory

class SetupWorkoutMuscleFragment : Fragment() {

    /**
     * The ViewModel @param {SetupWorkoutDifficultyViewModel}
     */
    private val viewModel by viewModels<SetupWorkoutMuscleViewModel> {
        SetupWorkoutMuscleViewModelFactory(
                MuscleRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).muscleDao()))
    }

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSetupWorkoutSelectionsBinding

    /**
     * The adapter for @param {RecyclerView}
     */
    private var selectionAdapter: SelectionAdapter? = null

    /**
     * Used to debug
     */
    init {
        Log.d(TAG, "Fragment created")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        //Create adapter by the Adapter class for RecyclerView
        // and give it the viewModel already created
        selectionAdapter = SelectionAdapter(viewModel)

        //Create the data binding and apply for all the view
        binding = DataBindingUtil.inflate<FragmentSetupWorkoutSelectionsBinding>(
                inflater,
                R.layout.fragment_setup_workout_selections,
                container,
                false
        ).apply {

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@SetupWorkoutMuscleFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            selectionViewModel = viewModel

            //Set the title
            titleSelection.text = resources.getString(R.string.title_setup_musle)

            //Bind the RecyclerView Adapter with the actual adapter
            selectionList.apply {
                adapter = selectionAdapter
                offscreenPageLimit = 3
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bindDescription((position + 1).toLong())
                    }
                })
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setPageTransformer(selectionCompositeTransformer())
            }

            //Set a NavigationUp Listener to the MaterialToolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
        }

        //observable for selectedCategoryId
        viewModel.selectedId.observe(this.viewLifecycleOwner, { selectedId ->
            selectedId?.let {
                viewModel.nextButtonEnable()
                //select the page of the currentItemSelected
                binding.selectionList.postDelayed({
                    binding.selectionList.currentItem = ((it.toInt()) - 1)
                }, 10)
                this.apply {
                    vibratePhone()
                    startClickEffect(AudioEffectsType.ADD_BUTTON)
                }
            }
        })

        //observable for the categories List
        viewModel.muscles.observe(this.viewLifecycleOwner, {
            selectionAdapter?.customSubmitListMuscle(it)
        })

        //observable for navigateNext
        viewModel.navigateNext.observe(this.viewLifecycleOwner, {
            it?.let {
                viewModel.selectedId.value?.let { _ ->
                    val newWorkoutSetup = SetupWorkoutMuscleFragmentArgs.fromBundle(requireArguments()).workoutSetup
                            .apply { muscle = viewModel.selectedId.value ?: 0 }
                    this.findNavController().navigate(
                            SetupWorkoutMuscleFragmentDirections.actionSetupWorkoutMuscleFragmentToExerciseBookFragment(
                                    newWorkoutSetup
                            )
                    )
                    viewModel.doneNavigating()
                }
            }
        })

        //observable live data for enabling the next button
        viewModel.nextButtonStatus.observe(this.viewLifecycleOwner, {
            it?.let {
                binding.nextButton.isEnabled = it
            }
        })

        return binding.root
    }

    private fun bindDescription(posId: Long?) {
        when (posId) {
            null -> viewModel.nextButtonDisable()
        }
        binding.selectionDetail.startAnimation(AnimationUtils.loadAnimation(requireContext(),
                R.anim.fade_in
        ))
    }

    override fun onDestroy() {
        super.onDestroy()
        selectionAdapter?.setLifecycleDestroyed()
    }

    /**
     * Companion Object used to debug
     */
    companion object {
        const val TAG = "SetWorkMuscleFragment"
    }
}