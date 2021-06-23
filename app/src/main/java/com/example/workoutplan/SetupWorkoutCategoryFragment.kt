package com.example.workoutplan


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.workoutplan.adapters.CategoryAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.category.CategoryRepository
import com.example.workoutplan.databinding.FragmentSetupWorkoutSelectionsBinding
import com.example.workoutplan.utilities.*
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModel
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModelFactory
import com.google.android.flexbox.*
import es.dmoral.toasty.Toasty
import kotlin.math.abs

class SetupWorkoutCategoryFragment : Fragment(){

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
    private lateinit var selectionAdapter: CategoryAdapter

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
        val viewModelFactory = SetupWorkoutCategoryViewModelFactory(
                CategoryRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).categoryDao()))

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this@SetupWorkoutCategoryFragment, viewModelFactory).get(
                SetupWorkoutCategoryViewModel::class.java)


        //Create adapter by the Adapter class for RecyclerView and give it the viewModel already created
        selectionAdapter = CategoryAdapter(viewModel)

        val composite = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
        }

        //Create the data binding and apply all the
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
                setPageTransformer(composite)
            }

            //Set a NavigationUp Listener to the MaterialToolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            nextButton.setOnClickListener {
                viewModel.onNavigateNext()
            }

            //Set the title
            titleSelection.text = resources.getString(R.string.pick_your_favorite_category)

            //Bind the currentItemSelected
            viewModel.selectedCategoryId.value?.let { id ->
                selectionList.postDelayed({
                    selectionList.currentItem = ((id.toInt()) - 1)
                }, 10)
            }
        }

        //observable for selectedCategoryId
        viewModel.selectedCategoryId.observe(this.viewLifecycleOwner, {
            checkButtonNext()
        })

        //observable for the categories List
        viewModel.categories.observe(this.viewLifecycleOwner, {
            selectionAdapter.customSubmitList(it)
        })

        //observable for navigateNext
        viewModel.navigateNext.observe(this.viewLifecycleOwner, {
            it?.let {
                viewModel.selectedCategoryId.value?.let { id ->
                    this.findNavController().navigate(SetupWorkoutCategoryFragmentDirections.actionSetupWorkoutCategoryFragmentToExerciseBookFragment())
                    viewModel.doneNavigating()
                }

            }
        })

        //observable for nextButtonEnabled
        viewModel.nextButtonStatus.observe(this.viewLifecycleOwner, {
        })

        return binding.root
    }

        private fun bindDescription(categoryId: Long?) {
            when(categoryId) {
                1L -> binding.selectionDetail.text = resources.getString(R.string.strength_description)
                2L -> binding.selectionDetail.text = resources.getString(R.string.cardio_description)
                3L -> binding.selectionDetail.text = resources.getString(R.string.healthy_description)
                null -> viewModel.nextButtonDisable()
            }
            binding.selectionDetail.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))

        }

    private fun checkButtonNext() {

        if(!viewModel.firstSelect) {
            viewModel.nextButtonEnable()
            viewModel.firstSelect = true
        }

        this.vibratePhone()
        this.startClickEffect(AudioEffectsType.ADD_BUTTON)
    }

    /**
     * Companion Object used to debug
     */
    companion object {
        const val TAG = "SetWorkCategoryFragment"
    }

    override fun onDestroy() {
        super.onDestroy()
        selectionAdapter.setLifecycleDestroyed()
    }
}
