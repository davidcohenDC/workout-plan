package com.example.workoutplan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.workoutplan.R
import com.example.workoutplan.SetupActivity
import com.example.workoutplan.databinding.FragmentDashboardBinding
import com.example.workoutplan.viewmodels.DashBoardViewModel

class DashboardFragment : Fragment() {

    private val viewModel: DashBoardViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate<FragmentDashboardBinding>(inflater,
                R.layout.fragment_dashboard,
                container,
                false).apply {
            lifecycleOwner = this@DashboardFragment.viewLifecycleOwner

        }

        viewModel.navigateNext.observe(viewLifecycleOwner, {
            requireActivity().run {
                startActivity(Intent(this, SetupActivity::class.java))
                finish()
            }
        })

        return binding.root
    }
}