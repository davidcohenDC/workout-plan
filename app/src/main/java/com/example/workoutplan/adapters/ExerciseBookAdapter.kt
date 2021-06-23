@file:Suppress("SpellCheckingInspection")

package com.example.workoutplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutplan.R
import com.example.workoutplan.data.category.Category
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.databinding.ListItemExerciseBinding
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_CATEGORY
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_HEADER
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_ITEM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseBookAdapter(
    private val clickAddListener: ExerciseAddListener,
    private val clickListener: ExerciseListener): ListAdapter<DataItem, ViewHolder>(ExerciseDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {

            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val exerciseItem = getItem(position) as DataItem.ExerciseItem
                holder.bind(exerciseItem.exercise, clickAddListener, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.ExerciseItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CategoryItem -> ITEM_VIEW_TYPE_CATEGORY
        }
    }

    fun customSubmitList(list: List<Exercise>?) {

        adapterScope.launch {
            val items = when(list) {
                null -> listOf(DataItem.Header)
                else -> list.map { DataItem.ExerciseItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(
        private val binding: ListItemExerciseBinding): RecyclerView.ViewHolder(binding.root) {

            fun bind(
                item: Exercise,
                clickAddListener: ExerciseAddListener,
                clickListener: ExerciseListener
            ) {
                binding.exercise = item
                binding.clickAddListener = clickAddListener
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class ExerciseAddListener(val clicklistener: (exercise: Exercise) -> Unit) {

    private var clicked = false

    fun onClick(exercise: Exercise) {
        if(clicked) return
        clicked = false
        clicklistener(exercise)
    }
}

class ExerciseListener(val clicklistener: (exercise: Exercise) -> Unit) {
    private var clicked = false

    fun onClick(exercise: Exercise) {
        if(clicked) return
        clicked = false
        clicklistener(exercise)
    }
}

class ExerciseDiffCallback: DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return  oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    abstract val id: Long

   data class ExerciseItem(val exercise: Exercise): DataItem() {
        override val id: Long
            get() = exercise.exerciseId
    }

    data class CategoryItem(val category: Category): DataItem() {
        override val id: Long
            get() = category.categoryId
    }

    object Header: DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

}