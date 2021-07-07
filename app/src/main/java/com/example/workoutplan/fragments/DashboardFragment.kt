package com.example.workoutplan.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.SetupActivity
import com.example.workoutplan.adapters.WorkoutAdapter
import com.example.workoutplan.adapters.WorkoutOpenListener
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.databinding.FragmentDashboardBinding
import com.example.workoutplan.viewmodels.HomeViewModel
class DashboardFragment : Fragment() {

    /**
     * The shared ViewModel @param {HomeViewModel}
     */
    private val viewModel: HomeViewModel by activityViewModels()

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentDashboardBinding

    /**
     * The adapter for exercises @param {RecyclerView}
     */
    private lateinit var adapterWorkouts: WorkoutAdapter

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

        //Create adapter by the Adapter class for RecyclerView
        // and give it the viewModel already created
        adapterWorkouts = WorkoutAdapter(
            workoutOpenListener = WorkoutOpenListener { 
                onWorkoutClickedHandle(it)
            })


        binding = DataBindingUtil.inflate<FragmentDashboardBinding>(inflater,
                R.layout.fragment_dashboard,
                container,
                false).apply {

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@DashboardFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            dashboardViewModel = viewModel
            workoutList.adapter = adapterWorkouts
        }

        viewModel.workouts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterWorkouts.customSubmitList(it)
            }
        })

        viewModel.navigateToSetupActivity.observe(viewLifecycleOwner, {
            it.let {
                requireActivity().run {
                    startActivity(Intent(this, SetupActivity::class.java))
                }
            }
        })

        return binding.root
    }

    private fun onWorkoutClickedHandle(workout: Workout) {
        viewModel.onNavigateToWorkoutExercisePage(workout)
    }

    companion object {
        const val TAG = "DashboardFragment"
    }
}