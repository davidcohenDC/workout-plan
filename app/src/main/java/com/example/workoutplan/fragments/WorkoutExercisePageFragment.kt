package com.example.workoutplan.fragments

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.databinding.FragmentWorkoutExercisePageBinding
import com.example.workoutplan.utilities.hideKeyboard
import com.example.workoutplan.utilities.nameFormat
import com.example.workoutplan.utilities.parseToInt
import com.example.workoutplan.viewmodels.WorkoutExercisePageViewModel
import com.example.workoutplan.viewmodels.factories.WorkoutExercisePageViewModelFactory
import es.dmoral.toasty.Toasty
import java.util.*

class WorkoutExercisePageFragment : Fragment() {

    /**
     * The ViewModel @param {ExercisePageViewModel}
     */
    private val viewModel by viewModels<WorkoutExercisePageViewModel> {
        WorkoutExercisePageViewModelFactory(
                WorkoutExerciseCrossRefRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).workoutExerciseCrossRefDao()),
                WorkoutExercisePageFragmentArgs.fromBundle(requireArguments()).workoutId,
                WorkoutExercisePageFragmentArgs.fromBundle(requireArguments()).exerciseId)
    }

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentWorkoutExercisePageBinding


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        //Create the data binding and apply for all the view
        binding = DataBindingUtil.inflate<FragmentWorkoutExercisePageBinding>(inflater,
                R.layout.fragment_workout_exercise_page,
                container,
                false
        ).apply {
            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@WorkoutExercisePageFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            exercisePageViewModel = viewModel

            //Set a NavigationUp Listener to the MaterialToolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            //Set a ShareIntent listener on action share
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    R.id.action_save -> {
                        viewModel.saveNewWorkoutDetail(
                                parseToInt(binding.setInput.text.toString()),
                                parseToInt(binding.repInput.text.toString()),
                                parseToInt(binding.durationInput.text.toString()))
                        this@WorkoutExercisePageFragment.hideKeyboard()
                        Toasty.info(requireContext(),resources.getString(R.string.exercisedetailed_updated), Toasty.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }
        //observable for button info
        binding.buttonMoreInfo.setOnClickListener {
            viewModel.exercise.value?.let {
                createSearchIntent(nameFormat(it.exerciseId))
            }
        }
        //observable for navigation back
        viewModel.navigateBack.observe(this.viewLifecycleOwner, { onNavBack ->
            onNavBack?.let {
                this.findNavController().navigate(WorkoutExercisesFragmentDirections.actionWorkoutExercisesFragmentToExercisePageFragment2())
                viewModel.doneNavigating()
            }

        })

        viewModel.exercise.observe(viewLifecycleOwner, {
            it?.let {
                binding.executePendingBindings()
            }
        })

        return binding.root
    }


    private fun createShareIntent() {
        val shareText = viewModel.exercise.value.let { exe ->
            if (exe == null) {
                ""
            } else {
                getString(R.string.share_text_exercise, exe.name)
            }
        }

        val shareIntent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ShareCompat.IntentBuilder.from(requireActivity())
                    .setText(shareText)
                    .setType("text/plain")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        startActivity(shareIntent)
    }

    private fun createSearchIntent(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)

        intent.putExtra(SearchManager.QUERY, String.format(resources.getString(R.string.query_search_exercise, query.toLowerCase(Locale.getDefault()))))
        startActivity(intent)
    }


}