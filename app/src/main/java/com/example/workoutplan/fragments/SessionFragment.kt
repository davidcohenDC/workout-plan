package com.example.workoutplan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.workoutplan.R
import com.example.workoutplan.adapters.SessionItemAdapter
import com.example.workoutplan.adapters.SessionItemListener
import com.example.workoutplan.adapters.items.SessionItem
import com.example.workoutplan.databinding.FragmentSessionBinding
import com.example.workoutplan.fragments.dialogs.ChangeSessionSetDialog
import com.example.workoutplan.utilities.getDrawable
import com.example.workoutplan.viewmodels.SessionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SessionFragment: Fragment() {

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by activityViewModels()

    /**
     * The Data Binding value to associate with the view
     */
    private lateinit var binding: FragmentSessionBinding

    private lateinit var adapterSessionItem: SessionItemAdapter

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

        adapterSessionItem = SessionItemAdapter(
            clickListener = SessionItemListener {
                onSessionItemClickHandler(it)
            }
        )

        binding = DataBindingUtil.inflate<FragmentSessionBinding>(inflater,
            R.layout.fragment_session,
            container,
            false).apply {

            recyclerSet.adapter = adapterSessionItem

            //Bind lifecycleOwner with the actual Fragment viewLifeCycle
            lifecycleOwner = this@SessionFragment.viewLifecycleOwner

            //Bind the selectionsViewModel with the actual viewModel
            sessionViewModel = viewModel

            setButtonsListener(this);
        }

        viewModel.navigateToWorkoutExercisePage.observe(viewLifecycleOwner) {
            //TODO
        }

        viewModel.navigateToEndSession.observe(viewLifecycleOwner) {
            //TODO
        }

        viewModel.testSet.observe(viewLifecycleOwner) {
            adapterSessionItem.customSubmitList(it)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            it?.let {
                if(it == SessionViewModel.Companion.STATUS.PAUSED) {
                    binding.btnStartStopSession.background = getDrawable(R.drawable.ic_btn_pause)
                } else {
                    binding.btnStartStopSession.background = getDrawable(R.drawable.ic_btn_play)
                }
            }
        }

        return binding.root
    }

    private fun onSessionItemClickHandler(sessionItem: SessionItem) {
        ChangeSessionSetDialog().show(childFragmentManager, "ChangeSessionSetFragment")

        Log.d(TAG,sessionItem.duration.toString())
        Log.d(TAG,sessionItem.repetition.toString())
    }

    private fun showEndSessionDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.leave_session_workout_title_dialog))
            .setMessage(getString(R.string.leave_session_workout))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.back)) { _, _ ->
                //NOTHING
            }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                //GO TO SUMMARY
            }
            .show()
    }

    private fun setButtonsListener(fragmentSessionBinding: FragmentSessionBinding?) {
            fragmentSessionBinding?.apply {

                btnStartStopSession.setOnClickListener { v ->
                    viewModel.toggleButton()
                    v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in))
                }
                btnNextExercise.setOnClickListener { v ->
                    v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in))
                }

                btnPrevExercise.setOnClickListener { v ->
                    v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in))
                }

                btnInfo.setOnClickListener { v ->
                    v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in))
                }

                btnStopSession.setOnClickListener {
                    showEndSessionDialog()
                }

            }
    }

    companion object {
        const val TAG = "SessionFragment"
    }


}