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
import com.example.workoutplan.databinding.EmptyFragmentBinding
import com.example.workoutplan.viewmodels.EmptyViewModel

class EmptyFragment : Fragment() {

    private lateinit var binding: EmptyFragmentBinding

    private val viewModel: EmptyViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate<EmptyFragmentBinding>(inflater,
                R.layout.empty_fragment,
                container,
                false).apply {

            lifecycleOwner = this@EmptyFragment.viewLifecycleOwner
            emptyViewModel = viewModel
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