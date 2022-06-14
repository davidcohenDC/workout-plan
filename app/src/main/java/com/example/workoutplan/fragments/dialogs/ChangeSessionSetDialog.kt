package com.example.workoutplan.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.workoutplan.R
import com.example.workoutplan.databinding.DialogSessionSetBinding
import com.example.workoutplan.viewmodels.SessionViewModel
import com.shawnlin.numberpicker.NumberPicker.SIDE_LINES
import es.dmoral.toasty.Toasty
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class ChangeSessionSetDialog : DialogFragment(){

    private lateinit var binding: DialogSessionSetBinding

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by viewModels ({requireParentFragment()})

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            //Get the layout inflater
            val inflater = requireActivity().layoutInflater

            binding = DataBindingUtil.inflate<DialogSessionSetBinding>(inflater,
                R.layout.dialog_session_set,
                null,
                true).apply {
                sessionViewModel = viewModel

                viewModel.sessionItemToEdit.value?.duration?.let { duration ->
                    numberPickerDuration.value = TimeUnit.SECONDS.toMinutes(duration).toInt()
                    txtTitleDuration.visibility = View.VISIBLE
                    dividerTitle3.visibility = View.VISIBLE
                    numberPickerDuration.visibility = View.VISIBLE
                }
                viewModel.sessionItemToEdit.value?.weight?.let { weight ->
                    numberPickerWeight.value = weight
                    txtTitleWeight.visibility = View.VISIBLE
                    dividerTitle2.visibility = View.VISIBLE
                    numberPickerWeight.visibility = View.VISIBLE
                }

                viewModel.sessionItemToEdit.value?.repetition?.let { rep ->
                    numberPickerRep.value = rep
                    txtTitleRep.visibility = View.VISIBLE
                    dividerStat1.visibility = View.VISIBLE
                    numberPickerRep.visibility = View.VISIBLE
                }


                executePendingBindings()
            }

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)

                //Add action buttons
                .setPositiveButton(R.string.confirm) { dialogInterface, i ->
                    viewModel.saveSessionItemToEdit(binding.numberPickerRep.value,binding.numberPickerDuration.value,binding.numberPickerWeight.value)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.unsetSessionItemToEdit()
        dismiss()
        super.onCancel(dialog)
    }

    override fun onDestroy() {
        viewModel.unsetSessionItemToEdit()
        super.onDestroy()
    }

}