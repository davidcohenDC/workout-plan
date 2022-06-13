package com.example.workoutplan.fragments

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.databinding.FragmentSessionSummaryBinding
import com.example.workoutplan.viewmodels.SessionSummaryViewModel
import com.example.workoutplan.viewmodels.factories.SummaryViewModelFactory
import java.util.concurrent.TimeUnit

class SessionSummaryFragment: Fragment() {

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionSummaryViewModel by activityViewModels() {
        SummaryViewModelFactory(
            SessionRepository(
                dao = WorkoutPlanDatabase.getInstance(
                    requireNotNull(activity).application
                ).sessionDao()
            )
        )
    }

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSessionSummaryBinding

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
        binding = DataBindingUtil.inflate<FragmentSessionSummaryBinding>(inflater,
        R.layout.fragment_session_summary,
        container,
            false).apply {


        btnSummaryNext.setOnClickListener {
            viewModel.onNavigateToHomePage()
        }


        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(SessionSummaryFragmentDirections.actionSessionSummaryFragment2ToHomeFragment2())
            }
            viewModel.navigateToHomePageDone()
        }

        viewModel.session.observe(viewLifecycleOwner) {
            it?.let {
                binding.textFirstDial.text = DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(it.duration))
                binding.textSecondDial.text = it.totalRepetitions.toString()
                binding.textThirdDial.text = it.totalSets.toString()
                val text = "${it.completed}%"
                binding.textFourDial.text = text
                when(it.rating) {
                    5 -> binding.lottieStars.setAnimation(R.raw.anim_get_five_starts)
                    4 -> binding.lottieStars.setAnimation(R.raw.anim_get_four_starss)
                    3 -> binding.lottieStars.setAnimation(R.raw.anim_get_three_stars)
                    2 -> binding.lottieStars.setAnimation(R.raw.anim_get_two_stars)
                    1 -> binding.lottieStars.setAnimation(R.raw.anim_get_one_star)
                }
            }
        }

        return binding.root
    }


    companion object {
        const val TAG = "SessionSummaryFragment"
    }

}