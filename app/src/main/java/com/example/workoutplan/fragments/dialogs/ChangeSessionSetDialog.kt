package com.example.workoutplan.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.workoutplan.R
import com.example.workoutplan.databinding.DialogSessionSetBinding
import com.example.workoutplan.viewmodels.SessionViewModel
import com.shawnlin.numberpicker.NumberPicker.SIDE_LINES
import es.dmoral.toasty.Toasty
import java.lang.IllegalStateException

class ChangeSessionSetDialog : DialogFragment(){

    private lateinit var binding: DialogSessionSetBinding

    /**
     * The shared ViewModel @param {SessionViewModel}
     */
    private val viewModel: SessionViewModel by activityViewModels()



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            //Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_session_set,null))

                //Add action buttons
                .setPositiveButton(R.string.confirm,DialogInterface.OnClickListener { dialogInterface, i ->
                })

             builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        dismiss()
        super.onCancel(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<DialogSessionSetBinding>(inflater,
        R.layout.dialog_session_set,
        container,
        false).apply {
            sessionViewModel = viewModel
        }
        return binding.root
    }
}