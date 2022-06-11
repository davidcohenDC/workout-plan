package com.example.workoutplan.fragments

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workoutplan.R
import com.example.workoutplan.adapters.SessionItemAdapter
import com.example.workoutplan.adapters.SessionItemListener
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.relations.SessionItem
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.databinding.FragmentSessionBinding
import com.example.workoutplan.fragments.dialogs.ChangeSessionSetDialog
import com.example.workoutplan.utilities.SESSION_ITEM_LIMIT
import com.example.workoutplan.utilities.getDrawable
import com.example.workoutplan.viewmodels.SessionViewModel
import com.example.workoutplan.viewmodels.factories.SessionViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

class SessionFragment: Fragment() {

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by activityViewModels<SessionViewModel> {
        SessionViewModelFactory(
            WorkoutExerciseCrossRefRepository(
                dao = WorkoutPlanDatabase.getInstance(
                    requireNotNull(activity).application
                ).workoutExerciseCrossRefDao()),
            SessionFragmentArgs.fromBundle(requireArguments()).workoutId
        )
    }

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
            clickListener = SessionItemListener { it,pos ->
                onSessionItemClickHandler(it,pos)
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

        viewModel.navigateToSummaryPage.observe(viewLifecycleOwner) {
            this.findNavController().navigate(SessionFragmentDirections.actionSessionFragment2ToSessionSummaryFragment2())
        }

        //I choose only the show only the actualExercise
        viewModel.sessionWorkout.observe(viewLifecycleOwner) { list ->
            viewModel.actualExerciseId.value?.let { adapterSessionItem.customSubmitList(viewModel.getSessionItemList(), it) }
            binding.executePendingBindings()
        }

        //in future only in xml
        viewModel.timeLeft.observe(viewLifecycleOwner) { it ->
            it.let {
                binding.textTimer.text = DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(it)+1)
                binding.progressBar.max = viewModel.timeTarget.toInt()
                binding.progressBar.progress = viewModel.timeTarget.toInt() - it.toInt()
            }
            binding.executePendingBindings()
        }


        viewModel.state.observe(viewLifecycleOwner) {
            binding.executePendingBindings()
            viewModel.actualExerciseId.value?.let { c -> adapterSessionItem.customSubmitList(viewModel.getSessionItemList(), c) }
            it?.let {
                if(it == SessionViewModel.Companion.TimerState.RUNNING) {
                    binding.btnStartStopSession.setBackgroundResource(R.drawable.ic_btn_pause)
                } else {
                    binding.btnStartStopSession.setBackgroundResource(R.drawable.ic_btn_play)
                }
            }
        }


        return binding.root
    }

    private fun onSessionItemClickHandler(sessionItem: SessionItem, position: Int) {
        if(sessionItem.status == SessionItem.Companion.STATUS.PROTO) {
            showAddSessionItemDialog()
        } else {
            viewModel.setSessionItemToEdit(sessionItem,position)
            ChangeSessionSetDialog().show(childFragmentManager, "ChangeSessionSetFragment")
        }
    }

    private fun showAddSessionItemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_set_to_session))
            .setMessage(getString(R.string.add_set_to_session_description))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                //NOTHING
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
/*                if(viewModel.sessionWorkout <= SESSION_ITEM_LIMIT) {
                    Log.i(TAG,viewModel.getSessionExerciseSessionItemSize().toString())
                    viewModel.addSessionItemToSession()
                } else {
                    Toasty.info(requireContext(),resources.getString(R.string.session_item_limit_reached),
                        Toasty.LENGTH_LONG).show()
                }*/

            }
            .show()
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
                this.findNavController().navigate(SessionFragmentDirections.actionSessionFragment2ToSessionSummaryFragment2())
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
                    viewModel.nextExercise()
                    v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in))
                }

                btnPrevExercise.setOnClickListener { v ->
                    viewModel.prevExercise()
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