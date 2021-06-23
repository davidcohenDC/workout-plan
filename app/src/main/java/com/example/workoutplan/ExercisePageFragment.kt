package com.example.workoutplan

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
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
import com.example.workoutplan.viewmodels.ExercisePageViewModelFactory
import es.dmoral.toasty.Toasty
import java.util.*

class ExercisePageFragment : Fragment(){

    private lateinit var viewModel: ExercisePageViewModel

    private lateinit var binding: FragmentExercisePageBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

         binding = DataBindingUtil.inflate<FragmentExercisePageBinding>(inflater,
                 R.layout.fragment_exercise_page,
                 container,
                 false
         ).apply {
             lifecycleOwner = this@ExercisePageFragment.viewLifecycleOwner

             val viewModelFactory = ExercisePageViewModelFactory(
                 ExerciseRepository(
                     dao = WorkoutPlanDatabase.getInstance(
                             requireNotNull(activity).application
                     ).ExerciseDao()),
                     ExercisePageFragmentArgs.fromBundle(requireArguments()).exerciseKey)

             viewModel = ViewModelProvider(this@ExercisePageFragment, viewModelFactory).get(
                     ExercisePageViewModel::class.java)

             exercisePageViewModel = viewModel

             toolbar.setNavigationOnClickListener { view ->
                 view.findNavController().navigateUp()

             }

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

        binding.buttonMoreInfo.setOnClickListener {
            viewModel.getExercise().value?.let {
                createSearchIntent(nameFormat(it))
            }
        }

        viewModel.navigateBack.observe(this.viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(ExercisePageFragmentDirections.actionExercisePageFragmentToExerciseBookFragment())
                viewModel.doneNavigating()
                startClickEffect(AudioEffectsType.BACK_BUTTON)
            }
        })

        return binding.root
    }

    private fun createShareIntent() {
        val shareText = viewModel.getExercise().value.let { exe ->
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