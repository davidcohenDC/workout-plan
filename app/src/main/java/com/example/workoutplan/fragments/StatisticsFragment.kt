package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.adapters.SessionAdapter
import com.example.workoutplan.adapters.SessionClickListener
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.databinding.FragmentStatisticsBinding
import com.example.workoutplan.viewmodels.HomeViewModel

class StatisticsFragment : Fragment() {

    /**
     * The shared ViewModel @param {HomeViewModel}
     */
    private val viewModel: HomeViewModel by activityViewModels()

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentStatisticsBinding

    /**
     * The adapter for exercises @param {RecyclerView}
     */
    private lateinit var adapterSessions: SessionAdapter


    /**
     * Used to debug
     */
    init {
        Log.d(DashboardFragment.TAG, "Fragment initialized")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Create adapter by the Adapter class for RecyclerView
        // and give it the viewModel already created
        adapterSessions = SessionAdapter(
            sessionClickListener = SessionClickListener {
                onSessionClickListener(it)
            })


        binding = DataBindingUtil.inflate<FragmentStatisticsBinding>(
            inflater,
            R.layout.fragment_statistics,
            container,
            false
        ).apply {

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@StatisticsFragment.viewLifecycleOwner

            sessionsList.adapter = adapterSessions
        }

        viewModel.sessions.observe(viewLifecycleOwner) {
            it?.let {
                adapterSessions.customSubmitList(it)
            }
        }

        return binding.root
    }

    private fun onSessionClickListener(session: Session) {
       viewModel.onNavigateToSummaryPage(session)
    }

    companion object {
        const val TAG = "StatisticsFragment"
    }
}