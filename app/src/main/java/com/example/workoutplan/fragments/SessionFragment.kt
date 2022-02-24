package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.workoutplan.R
import com.example.workoutplan.databinding.FragmentSessionBinding
import com.example.workoutplan.viewmodels.SessionViewModel

class SessionFragment: Fragment() {

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by activityViewModels()

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSessionBinding

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
        binding = DataBindingUtil.inflate<FragmentSessionBinding>(inflater,
            R.layout.fragment_session,
            container,
            false).apply {

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@SessionFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            sessionViewModel = viewModel
        }

        viewModel.navigateToWorkoutExercisePage.observe(viewLifecycleOwner) {
            //TODO
        }

        viewModel.navigateToEndSession.observe(viewLifecycleOwner) {
            //TODO
        }

        return binding.root
    }

    companion object {
        const val TAG = "SessionFragment"
    }

}