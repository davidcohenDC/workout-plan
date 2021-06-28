package com.example.workoutplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.databinding.ListItemExerciseSelectedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseSelectedAdapter(
        private val clickListener: ExerciseSelectedListener
): ListAdapter<Exercise, ExerciseSelectedAdapter.ExerciseSelectedHolder>(ExerciseSelectedDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSelectedHolder {
        return ExerciseSelectedHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExerciseSelectedHolder, position: Int) {
            val item = getItem(position) as Exercise
            holder.bind(item, clickListener)
    }

    fun customSubmitList(list: MutableList<Exercise>?) {
        adapterScope.launch {
            val items = list?.map { it }?.toList()
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyItemInserted(itemCount)
            }
        }
    }

    class ExerciseSelectedHolder private constructor(
            private val binding: ListItemExerciseSelectedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
                item: Exercise,
                clickListener: ExerciseSelectedListener
        ) {
            binding.clickListener = clickListener
            binding.exercise = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ExerciseSelectedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseSelectedBinding.inflate(layoutInflater, parent, false)
                return ExerciseSelectedHolder(binding)
            }
        }
    }


}

class ExerciseSelectedListener(val clicklistener: (exercise: Exercise) -> Unit) {
    private var clicked = false

    fun onClick(exercise: Exercise) {
        if(clicked) return
        clicked = false
        clicklistener(exercise)
    }
}

class ExerciseSelectedDiffCallback: DiffUtil.ItemCallback<Exercise>() {

    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return  oldItem.exerciseId == newItem.exerciseId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }
}
