package com.example.workoutplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.databinding.ListItemWorkoutExerciseBinding
import com.example.workoutplan.utilities.ItemAnimation
import com.example.workoutplan.viewmodels.WorkoutExercisesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseDetailedAdapter(
        private val clickListener: ExerciseDetailedListener,
        private val viewModel: WorkoutExercisesViewModel
) : ListAdapter<ExerciseDetailed, ExerciseDetailedAdapter.ExerciseDetailedHolder>(ExerciseDetailedDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private val viewDetailedHolders: MutableList<ExerciseDetailedHolder> = mutableListOf()
    private var lastPosition = -1
    private var onAttach = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailedHolder {
        return ExerciseDetailedHolder.from(parent, viewDetailedHolders)
    }

    override fun onBindViewHolder(detailedHolder: ExerciseDetailedHolder, position: Int) {
        val exerciseItem = getItem(position) as ExerciseDetailed
        setAnimation(detailedHolder.itemView, position)
        detailedHolder.bind(exerciseItem, clickListener, viewModel)
    }

    override fun onViewAttachedToWindow(detailedHolder: ExerciseDetailedHolder) {
        super.onViewAttachedToWindow(detailedHolder)
        detailedHolder.markAttach()
    }

    override fun onViewDetachedFromWindow(detailedHolder: ExerciseDetailedHolder) {
        super.onViewDetachedFromWindow(detailedHolder)
        detailedHolder.markDetach()
    }

    fun customSubmitList(list: List<ExerciseDetailed>?) {

        adapterScope.launch {
            val items = when (list) {
                null -> listOf()
                else -> list.map { it }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyDataSetChanged()
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(view: View, position: Int) {
        if(position > lastPosition) {
            ItemAnimation.animate(view,if (onAttach) position else -1, ItemAnimation.LEFT_RIGHT)
            lastPosition = position
        }
    }

    class ExerciseDetailedHolder private constructor(
            private val binding: ListItemWorkoutExerciseBinding,
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)
        private var wasPaused: Boolean = false


        fun markCreated() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        fun markAttach() {
            if (wasPaused) {
                lifecycleRegistry.currentState = Lifecycle.State.RESUMED
            } else {
                lifecycleRegistry.currentState = Lifecycle.State.STARTED
            }
        }

        fun markDetach() {
            wasPaused = true
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        fun markDestroyed() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        fun bind(
                item: ExerciseDetailed,
                clickListener: ExerciseDetailedListener,
                viewModel: WorkoutExercisesViewModel
        ) {
            binding.homeViewModel = viewModel
            binding.exercise = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        companion object {
            fun from(parent: ViewGroup, viewDetailedHolders: MutableList<ExerciseDetailedHolder>): ExerciseDetailedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWorkoutExerciseBinding.inflate(layoutInflater, parent, false)
                val viewHolder = ExerciseDetailedHolder(binding)
                binding.lifecycleOwner = viewHolder
                viewHolder.markCreated()
                viewDetailedHolders.add(viewHolder)
                return viewHolder
            }
        }
    }
}


class ExerciseDetailedListener(val clicklistener: (exerciseDetailed: ExerciseDetailed) -> Unit) {
    private var clicked = false

    fun onClick(exerciseDetailed: ExerciseDetailed) {
        if (clicked) return
        clicked = false
        clicklistener(exerciseDetailed)
    }
}

class ExerciseDetailedDiffCallback : DiffUtil.ItemCallback<ExerciseDetailed>() {

    override fun areItemsTheSame(oldItem: ExerciseDetailed, newItem: ExerciseDetailed): Boolean {
        return oldItem.exerciseId == newItem.exerciseId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ExerciseDetailed, newItem: ExerciseDetailed): Boolean {
        return oldItem == newItem
    }
}
