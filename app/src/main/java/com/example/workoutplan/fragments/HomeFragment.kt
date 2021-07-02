package com.example.workoutplan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.R
import com.example.workoutplan.adapters.HomePagerAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.databinding.FragmentHomeBinding
import com.example.workoutplan.utilities.getDrawable
import com.example.workoutplan.viewmodels.HomeViewModel
import com.example.workoutplan.viewmodels.factories.HomeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    /**
     * The ViewModel @param {HomeViewModel}
     */
    private lateinit var viewModel: HomeViewModel

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentHomeBinding

    /**
     * The adapter for @param {HomePagerAdapter}
     */
    private lateinit var pageAdapter: HomePagerAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        /**
         * //Create a viewModelFactory with the @param {ViewModelProvider} and associate with is Dao
         */
        val viewModelFactory = HomeViewModelFactory(
                WorkoutRepository(
                        dao = WorkoutPlanDatabase.getInstance(
                                requireNotNull(activity).application
                        ).workoutDao()))

        //Create viewModel by ViewModelProvider to the view
        viewModel = ViewModelProvider(this, viewModelFactory).get(
                HomeViewModel::class.java)

        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
                R.layout.fragment_home,
                container,
                false).apply {
            lifecycleOwner = this@HomeFragment.viewLifecycleOwner
        }

        //This observe is used to wait the data for repository that return the number of workouts
        viewModel.workoutSize.observe(viewLifecycleOwner, Observer { nWorkouts ->

            nWorkouts?.let {
                pageAdapter = if (nWorkouts > 1) {
                    HomePagerAdapter(this).apply {
                        getDrawable(R.drawable.dashboard)?.let { addFragment(DashboardFragment(), "Dashboard", it) }
                        getDrawable(R.drawable.statistics)?.let { addFragment(DashboardFragment(), "Statistics", it) }
                    }
                } else {
                    HomePagerAdapter(this).apply {
                        getDrawable(R.drawable.dashboard)?.let { addFragment(EmptyFragment(), "Dashboard", it) }
                        getDrawable(R.drawable.statistics)?.let { addFragment(EmptyFragment(), "Statistics", it) }
                    }
                }
            }

            binding.viewPager.adapter = pageAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = pageAdapter.getPageTitle(position)
                tab.icon = pageAdapter.getPageIcon(position)
            }.attach()
        })

        return binding.root
    }

}