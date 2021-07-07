package com.example.workoutplan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.workoutplan.MainActivity
import com.example.workoutplan.R
import com.example.workoutplan.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate<FragmentAboutBinding>(inflater,
        R.layout.fragment_about,
        container,
        false).apply {
            lifecycleOwner = this@AboutFragment.viewLifecycleOwner

        }
        (requireActivity() as MainActivity).toogleMenu(binding.toolbar)
        return binding.root
    }
}