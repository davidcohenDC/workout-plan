package com.example.workoutplan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.workoutplan.adapters.SelectionAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.difficulty.DifficultyRepository
import com.example.workoutplan.databinding.FragmentSetupWorkoutSelectionsBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.selectionCompositeTransformer
import com.example.workoutplan.utilities.startClickEffect
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.SetupWorkoutDifficultyViewModel
import com.example.workoutplan.viewmodels.factories.SetupWorkoutDifficultyViewModelFactory

class SetupWorkoutDifficultyFragment: Fragment() {

    /**
     * The ViewModel @param {SetupWorkoutDifficultyViewModel}
     */
    private lateinit var viewModel: SetupWorkoutDifficultyViewModel

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

        /**
         * //Create a viewModelFactory with the @param {ViewModelProvider} and associate with is Dao
         */
        val viewModelFactory = SetupWorkoutDifficultyViewModelFactory(
                DifficultyRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).difficultyDao()))

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this@SetupWorkoutDifficultyFragment, viewModelFactory).get(
                SetupWorkoutDifficultyViewModel::class.java)

        //Create adapter by the Adapter class for RecyclerView and give it the viewModel already created
        selectionAdapter = SelectionAdapter(viewModel)

        binding = DataBindingUtil.inflate<FragmentSetupWorkoutSelectionsBinding>(
                inflater,
                R.layout.fragment_setup_workout_selections,
                container,
                false
        ).apply {

            lifecycleOwner = this@SetupWorkoutDifficultyFragment.viewLifecycleOwner

            selectionViewModel = viewModel

            //Set the title
            titleSelection.text = resources.getString(R.string.choose_your_difficulty)

            //Bind the RecyclerView Adapter with the actual adapter
            selectionList.apply {
                adapter = selectionAdapter
                offscreenPageLimit = 3
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bindDescription((position+1).toLong())
                    }
                })
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setPageTransformer(selectionCompositeTransformer())
            }

            //Set a NavigationUp Listener to the MaterialToolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            //Set a NavigationUp Listener to the MaterialToolbar
            nextButton.setOnClickListener {
                viewModel.onNavigateNext()
            }
        }

        //observable for selectedCategoryId
        viewModel.selectedId.observe(this.viewLifecycleOwner, { selectedId ->
            selectedId?.let { id ->
                viewModel.nextButtonEnable()
                //select the page of the currentItemSelected
                binding.selectionList.postDelayed({
                    binding.selectionList.currentItem = ((id.toInt()) - 1)
                },10)
                this.apply {
                    vibratePhone()
                    startClickEffect(AudioEffectsType.ADD_BUTTON)
                }
            }
        })

        //observable for the categories List
        viewModel.difficulties.observe(this.viewLifecycleOwner, {
            selectionAdapter?.customSubmitListDifficulty(it)
        })

        //observable for navigateNext
        viewModel.navigateNext.observe(this.viewLifecycleOwner, { onNavigate ->
            onNavigate?.let {
                viewModel.selectedId.value?.let { _ ->
                    val newWorkoutSetup = SetupWorkoutDifficultyFragmentArgs
                            .fromBundle(requireArguments()).workoutSetup.also {
                        it.difficulty = viewModel.getSelectedId() }
                    this.findNavController().navigate(SetupWorkoutDifficultyFragmentDirections
                            .actionSetupWorkoutDifficultyFragmentToSetupWorkoutMuscleFragment(newWorkoutSetup))
                    viewModel.doneNavigating()
                }
            }
        })

        return binding.root
    }

    private fun bindDescription(posId: Long?) {
        when(posId) {
            1L -> binding.selectionDetail.text = resources.getString(R.string.beginner_description)
            2L -> binding.selectionDetail.text = resources.getString(R.string.intermediate_description)
            3L -> binding.selectionDetail.text = resources.getString(R.string.advance_description)
            4L -> binding.selectionDetail.text = resources.getString(R.string.expert_description)
            null -> viewModel.nextButtonDisable()
        }
        binding.selectionDetail.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))
    }


    override fun onDestroy() {
        super.onDestroy()
        selectionAdapter?.setLifecycleDestroyed()
    }

    /**
     * Companion Object used to debug
     */
    companion object {
        const val TAG = "SetWorkDiffFragment"
    }

}