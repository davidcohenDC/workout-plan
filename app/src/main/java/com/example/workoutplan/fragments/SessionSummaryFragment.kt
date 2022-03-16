package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.workoutplan.R
import com.example.workoutplan.databinding.FragmentSessionSummaryBinding
import com.example.workoutplan.viewmodels.SessionSummaryViewModel

class SessionSummaryFragment: Fragment() {

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionSummaryViewModel by activityViewModels()

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
    ): View? {
        binding = DataBindingUtil.inflate<FragmentSessionSummaryBinding>(inflater,
        R.layout.fragment_session_summary,
        container,
            false).apply {

        }
        return binding.root
    }

    companion object {
        const val TAG = "SessionSummaryFragment"
    }

}