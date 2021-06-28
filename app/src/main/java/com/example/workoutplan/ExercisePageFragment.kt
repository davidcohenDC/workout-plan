package com.example.workoutplan

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.data.exercise.ExerciseRepository
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.databinding.FragmentExercisePageBinding
import com.example.workoutplan.utilities.AudioEffectsType
import com.example.workoutplan.utilities.nameFormat
import com.example.workoutplan.utilities.startClickEffect
import com.example.workoutplan.viewmodels.ExercisePageViewModel
import com.example.workoutplan.viewmodels.factories.ExercisePageViewModelFactory
import java.util.*

class ExercisePageFragment : Fragment(){

    /**
     * The ViewModel @param {ExercisePageViewModel}
     */
    private lateinit var viewModel: ExercisePageViewModel

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentExercisePageBinding

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
        val viewModelFactory = ExercisePageViewModelFactory(
                ExerciseRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).ExerciseDao()),
                ExercisePageFragmentArgs.fromBundle(requireArguments()).exerciseKey)

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this@ExercisePageFragment, viewModelFactory).get(
                ExercisePageViewModel::class.java)

        //Create the data binding and apply for all the view
         binding = DataBindingUtil.inflate<FragmentExercisePageBinding>(inflater,
                 R.layout.fragment_exercise_page,
                 container,
                 false
         ).apply {

             //Bind lifecycleOwner with the actual Fragment viewLifeCycle
             lifecycleOwner = this@ExercisePageFragment.viewLifecycleOwner

             //Bind the selectionsViewModel with the actual viewModel
             exercisePageViewModel = viewModel

             //Set a NavigationUp Listener to the MaterialToolbar
             toolbar.setNavigationOnClickListener { view ->
                 view.findNavController().navigateUp()
             }

             //Set a ShareIntent listener on action share
             toolbar.setOnMenuItemClickListener { item ->
                 when(item.itemId) {
                     R.id.action_share -> {
                         createShareIntent()
                         true
                     }
                     else -> false
                 }
             }
         }

        //observable for button info
        binding.buttonMoreInfo.setOnClickListener {
            viewModel.exercise.value?.let {
                createSearchIntent(nameFormat(it))
            }
        }

        //observable for navigation back
        viewModel.navigateBack.observe(this.viewLifecycleOwner, { onNavBack ->
            onNavBack?.let {
                if (onNavBack) {
                    this.findNavController().navigate(ExercisePageFragmentDirections
                            .actionExercisePageFragmentToExerciseBookFragment(
                                    ExercisePageFragmentArgs.fromBundle(requireArguments()
                                    ).workoutSetup))
                    this.startClickEffect(AudioEffectsType.BACK_BUTTON)
                    viewModel.doneNavigating()
                }
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

    private fun createSearchIntent(query : String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)

        intent.putExtra(SearchManager.QUERY,String.format(resources.getString(R.string.query_search_exercise, query.toLowerCase(Locale.getDefault()))))
        startActivity(intent)
    }

    companion object {
        const val TAG = "ExercisePageFragment"
    }
}