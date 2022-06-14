package com.example.workoutplan.fragments

import android.os.Bundle
import android.se.omapi.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.MainActivity
import com.example.workoutplan.R
import com.example.workoutplan.adapters.HomePagerAdapter
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.databinding.FragmentHomeBinding
import com.example.workoutplan.utilities.getDrawable
import com.example.workoutplan.viewmodels.HomeViewModel
import com.example.workoutplan.viewmodels.factories.HomeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    /**
     * The Shared ViewModel @param {HomeViewModel}
     */
    private val viewModel by activityViewModels<HomeViewModel> {
        HomeViewModelFactory(
                WorkoutRepository(
                dao = WorkoutPlanDatabase.getInstance(
                        requireNotNull(activity).application
                ).workoutDao()),
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

        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
                R.layout.fragment_home,
                container,
                false).apply {
            lifecycleOwner = this@HomeFragment.viewLifecycleOwner
        }

        (requireActivity() as MainActivity).toogleMenu(binding.toolbar)
        val ciao = StatisticsFragment()
        ciao.onStart()
        //This observe is used to wait the data for repository that return the number of workouts
        viewModel.workouts.observe(viewLifecycleOwner) { nWorkouts ->
            nWorkouts?.let {


                pageAdapter = if (nWorkouts.isNotEmpty()) {
                    HomePagerAdapter(this).apply {
                        getDrawable(R.drawable.dashboard)?.let {
                            addFragment(
                                DashboardFragment(),
                                "Dashboard",
                                it
                            )
                        }
                        getDrawable(R.drawable.statistics)?.let {
                            addFragment(
                                ciao,
                                "Statistics",
                                it
                            )
                        }
                    }
                } else {
                    HomePagerAdapter(this).apply {
                        getDrawable(R.drawable.dashboard)?.let {
                            addFragment(
                                EmptyFragment(),
                                "Dashboard",
                                it
                            )
                        }
                        getDrawable(R.drawable.statistics)?.let {
                            addFragment(
                                EmptyFragment(),
                                "Statistics",
                                it
                            )
                        }
                    }
                }
            }

            binding.viewPager.adapter = pageAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = pageAdapter.getPageTitle(position)
                tab.icon = pageAdapter.getPageIcon(position)
            }.attach()
        }


        viewModel.navigateToWorkoutExercisePage.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragment2ToWorkoutExercisesFragment(it)
                )
                viewModel.navigateToWorkoutExercisePageDone()
            }
        }
        viewModel.navigateToWorkoutExercisePage.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragment2ToWorkoutExercisesFragment(it)
                )
                viewModel.navigateToWorkoutExercisePageDone()
            }
        }

        viewModel.navigateTSummaryPage.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragment2ToSessionSummaryFragment2(it)

                )
                viewModel.onNavigateToSummaryPageDone()
            }
        }


        return binding.root
    }



}