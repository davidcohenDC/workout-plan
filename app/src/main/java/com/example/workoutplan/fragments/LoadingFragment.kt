package com.example.workoutplan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.MainActivity
import com.example.workoutplan.R
import com.example.workoutplan.databinding.FragmentHomeBinding
import com.example.workoutplan.databinding.FragmentLoadingBinding
import com.example.workoutplan.viewmodels.LoadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoadingFragment : Fragment() {


    private lateinit var viewModel: LoadingViewModel

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate<FragmentLoadingBinding>(inflater,
        R.layout.fragment_loading,
        container,
        false).apply {
            lifecycleOwner = this@LoadingFragment.viewLifecycleOwner

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadingViewModel::class.java)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(2500)
                try {
                    withContext(Dispatchers.Main) {
                        requireActivity().run {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                } catch  (e: IOException) {
                    }
            }
        }
    }
}