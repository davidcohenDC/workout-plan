package com.example.workoutplan.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.databinding.ListItemWorkoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutAdapter(
    private val workoutOpenListener: WorkoutOpenListener
) : ListAdapter<Workout, WorkoutAdapter.WorkoutHolder>(WorkoutDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        return WorkoutHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
        val workoutItem = getItem(position) as Workout
        holder.bind(workoutItem, workoutOpenListener)
    }

    fun customSubmitList(list: List<Workout>?) {

        adapterScope.launch {
            val items = when(list) {
                null -> listOf<Workout>()
                else -> list.map { it }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyDataSetChanged()
            }
        }
    }


    class WorkoutHolder private constructor(
        private val binding: ListItemWorkoutBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Workout,
            workoutOpenListener: WorkoutOpenListener
        ) {
            binding.workout = item
            binding.clickOpenListener = workoutOpenListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWorkoutBinding.inflate(layoutInflater, parent,false)
                return WorkoutHolder(binding)
            }
        }
    }


}


class WorkoutOpenListener(val clickListener: (workout: Workout) -> Unit) {
    private var clicked = false

    fun onClick(workout: Workout) {
        if (clicked) return
        clicked = false
        clickListener(workout)
    }
}

class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {

    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.workoutId == newItem.workoutId
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }

}
