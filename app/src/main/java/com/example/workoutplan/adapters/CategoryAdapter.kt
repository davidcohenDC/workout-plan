package com.example.workoutplan.adapters

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.INVISIBLE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutplan.R
import com.example.workoutplan.data.category.Category
import com.example.workoutplan.databinding.ListItemCategoryBinding
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_CATEGORY
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_HEADER
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_ITEM
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class CategoryAdapter(
        private val viewModel: SetupWorkoutCategoryViewModel
        ): ListAdapter<DataItem, CategoryAdapter.DataBoundViewHolder>(CategoryDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private val viewHolders: MutableList<DataBoundViewHolder> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): DataBoundViewHolder {
       return when(viewtype) {
           ITEM_VIEW_TYPE_CATEGORY -> DataBoundViewHolder.from(parent, viewHolders) as DataBoundViewHolder
           else -> throw ClassCastException("Unknown viewType $viewtype")
       }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
            val categoryItem = getItem(position) as DataItem.CategoryItem
            holder.bind(categoryItem.category, viewModel)
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.ExerciseItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CategoryItem -> ITEM_VIEW_TYPE_CATEGORY
        }
    }

    override fun onViewAttachedToWindow(holder: DataBoundViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: DataBoundViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    fun customSubmitList(list: List<Category>) {
        adapterScope.launch {

            val items = list.map { DataItem.CategoryItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun setLifecycleDestroyed() {
        viewHolders.forEach { it.markDestroyed() }
    }

    class DataBoundViewHolder private constructor(
            private val binding: ListItemCategoryBinding): RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)
        private var wasPaused: Boolean = false

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun markCreated() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        fun markAttach() {
            if(wasPaused) {
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
                item: Category,
                viewModel: SetupWorkoutCategoryViewModel
        ) {

            binding.categoryViewModel = viewModel
            binding.category = item
            binding.executePendingBindings()
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        companion object {
            fun from(parent: ViewGroup, viewHolders: MutableList<DataBoundViewHolder>): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(layoutInflater,parent,false)
                val viewHolder = DataBoundViewHolder(binding)
                binding.lifecycleOwner = viewHolder
                binding.categoryCard.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) {
                        binding.categoryCard.startAnimation(AnimationUtils.loadAnimation(parent.context, R.anim.rotate))
                    }
                }
                viewHolder.markCreated()
                viewHolders.add(viewHolder)
                return viewHolder
            }
        }
    }
}

class CategoryDiffCallback: DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
       return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}



