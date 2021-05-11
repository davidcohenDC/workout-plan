package com.example.workoutplan

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.category.CategoryRepository
import com.example.workoutplan.databinding.FragmentSetupWorkoutCategoryBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.startClickEffect
import com.example.workoutplan.utilities.vibratePhone
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModel
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModelFactory
import com.example.workoutplan.viewmodels.SetupWorkoutTitleViewModel
import kotlin.math.log

class SetupWorkoutCategoryFragment : Fragment(){

    private lateinit var viewModel: SetupWorkoutCategoryViewModel

    private lateinit var binding: FragmentSetupWorkoutCategoryBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate<FragmentSetupWorkoutCategoryBinding>(
                inflater,
                R.layout.fragment_setup_workout_category,
                container,
                false
        ).apply {

            lifecycleOwner = this@SetupWorkoutCategoryFragment.viewLifecycleOwner

            val viewModelFactory = SetupWorkoutCategoryViewModelFactory(
                CategoryRepository(
                    dao = WorkoutPlanDatabase.getInstance(
                        requireNotNull(activity).application
                    ).categoryDao()))

            viewModel = ViewModelProvider(this@SetupWorkoutCategoryFragment, viewModelFactory).get(
                SetupWorkoutCategoryViewModel::class.java)

            setupViewModel = viewModel
            executePendingBindings()

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()

            }
        }

        viewModel.selectedCategory.observe(this.viewLifecycleOwner, {
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
                when(it) {
                    1L -> {
                        binding.cardFirst.isChecked = true
                        binding.cardSecond.isChecked = false
                        binding.cardThird.isChecked = false
                        binding.imageFirst.startAnimation(anim)
                        viewModel.nextButtonEnable()

                    }
                    2L -> {
                        binding.cardFirst.isChecked = false
                        binding.cardSecond.isChecked = true
                        binding.cardThird.isChecked = false
                        binding.imageSecond.startAnimation(anim)
                        viewModel.nextButtonEnable()
                    }
                    3L -> {
                        binding.cardFirst.isChecked = false
                        binding.cardSecond.isChecked = false
                        binding.cardThird.isChecked = true
                        binding.imageThirdimageThird.startAnimation(anim)
                        viewModel.nextButtonEnable()
                    }
                    null -> {
                        binding.cardFirst.isChecked = false
                        binding.cardSecond.isChecked = false
                        binding.cardThird.isChecked = false
                        viewModel.nextButtonDisable()
                    }
                }
            vibratePhone()
            startClickEffect(AudioEffectsType.ADD_BUTTON)
        })

        viewModel.navigateNext.observe(this.viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(SetupWorkoutCategoryFragmentDirections.actionSetupWorkoutCategoryFragmentToExerciseBookFragment())
                viewModel.doneNavigating()
            }
        })

        viewModel.nextButtonEnabled.observe(this.viewLifecycleOwner, {
            it?.let {
                binding.nextButton.isEnabled = it
            }
        })

        return binding.root
    }
}