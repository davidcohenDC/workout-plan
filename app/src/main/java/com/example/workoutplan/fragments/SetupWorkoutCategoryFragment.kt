package com.example.workoutplan.fragments

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
import com.example.workoutplan.R
import com.example.workoutplan.adapters.SelectionAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.CategoryRepository
import com.example.workoutplan.databinding.FragmentSetupWorkoutSelectionsBinding
import com.example.workoutplan.utilities.*
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModel
import com.example.workoutplan.viewmodels.factories.SetupWorkoutCategoryViewModelFactory

class SetupWorkoutCategoryFragment : Fragment() {

    /**
     * The ViewModel @param {SetupWorkoutCategoryViewModel}
     */
    private lateinit var viewModel: SetupWorkoutCategoryViewModel

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
        Log.d(TAG, "Fragment initialized")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        /**
         * //Create a viewModelFactory with the @param {ViewModelProvider} and associate with is Dao
         */
        val viewModelFactory = SetupWorkoutCategoryViewModelFactory(
                CategoryRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).categoryDao()))

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this@SetupWorkoutCategoryFragment, viewModelFactory).get(
                SetupWorkoutCategoryViewModel::class.java)

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
            lifecycleOwner = this@SetupWorkoutCategoryFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            selectionViewModel = viewModel

            //Set the title
            titleSelection.text = resources.getString(R.string.pick_your_favorite_category)

            //Bind the RecyclerView Adapter with the actual adapter
            selectionList.apply {
                adapter = selectionAdapter
                offscreenPageLimit = 3
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bindDescriptionText((position + 1).toLong())
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
        viewModel.categories.observe(this.viewLifecycleOwner, {
            selectionAdapter?.customSubmitListCategory(it)
        })

        //observable for navigateNext
        viewModel.navigateNext.observe(this.viewLifecycleOwner, { onNavigate ->
            onNavigate?.let {
                viewModel.selectedId.value?.let { _ ->
                    val newWorkoutSetup = SetupWorkoutCategoryFragmentArgs.fromBundle(
                            requireArguments()
                    ).workoutSetup
                            .apply { category = viewModel.getSelectedId() }
                    this.findNavController().navigate(
                            SetupWorkoutCategoryFragmentDirections.actionSetupWorkoutCategoryFragmentToSetupWorkoutDifficultyFragment(
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

    private fun bindDescriptionText(position: Long?) {
        when (position) {
            1L -> {
                binding.selectionDetail.text = resources.getString(R.string.strength_description)
            }
            2L -> {
                binding.selectionDetail.text = resources.getString(R.string.cardio_description)
            }
            3L -> {
                binding.selectionDetail.text = resources.getString(R.string.healthy_description)
            }
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
        const val TAG = "SetWorkCategoryFragment"
    }
}
