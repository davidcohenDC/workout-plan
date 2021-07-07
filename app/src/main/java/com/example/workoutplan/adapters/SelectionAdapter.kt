package com.example.workoutplan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutplan.R
import com.example.workoutplan.adapters.items.SelectionItem
import com.example.workoutplan.data.entity.Category
import com.example.workoutplan.data.entity.Difficulty
import com.example.workoutplan.data.entity.Muscle
import com.example.workoutplan.databinding.ListItemSelectionBinding
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_CATEGORY
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_DIFFICULTY
import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_MUSCLE
import com.example.workoutplan.viewmodels.SelectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectionAdapter(
        private val viewModel: SelectionViewModel,
) : ListAdapter<SelectionItem, SelectionAdapter.DataBoundViewHolder>(CategoryDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private val viewHolders: MutableList<DataBoundViewHolder> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): DataBoundViewHolder {
        return when (viewtype) {
            ITEM_VIEW_TYPE_CATEGORY, ITEM_VIEW_TYPE_DIFFICULTY, ITEM_VIEW_TYPE_MUSCLE ->
                DataBoundViewHolder.from(parent, viewHolders) as DataBoundViewHolder
            else -> throw ClassCastException("Unknown viewType $viewtype")
        }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SelectionItem.CategoryItem -> holder.bindCategory(item, viewModel)
            is SelectionItem.DifficultyItem -> holder.bindDifficulty(item, viewModel)
            is SelectionItem.MuscleItem -> holder.bindMuscle(item, viewModel)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SelectionItem.CategoryItem -> ITEM_VIEW_TYPE_CATEGORY
            is SelectionItem.DifficultyItem -> ITEM_VIEW_TYPE_DIFFICULTY
            is SelectionItem.MuscleItem -> ITEM_VIEW_TYPE_MUSCLE
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

    fun customSubmitListCategory(list: List<Category>) {
        adapterScope.launch {

            val items = list.map { SelectionItem.CategoryItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun customSubmitListDifficulty(list: List<Difficulty>) {
        adapterScope.launch {

            val items = list.map { SelectionItem.DifficultyItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun customSubmitListMuscle(list: List<Muscle>) {
        adapterScope.launch {

            val items = list.map { SelectionItem.MuscleItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun setLifecycleDestroyed() {
        viewHolders.forEach { it.markDestroyed() }
    }

    class DataBoundViewHolder private constructor(
            private val binding: ListItemSelectionBinding,
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)
        private var wasPaused: Boolean = false

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

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

        fun bindCategory(
                item: SelectionItem.CategoryItem,
                viewModel: SelectionViewModel,
        ) {

            binding.selectionViewModel = viewModel
            binding.selection = item
            binding.executePendingBindings()
        }

        fun bindDifficulty(
                item: SelectionItem.DifficultyItem,
                viewModel: SelectionViewModel,
        ) {

            binding.selectionViewModel = viewModel
            binding.selection = item
            binding.executePendingBindings()
        }

        fun bindMuscle(
                item: SelectionItem.MuscleItem,
                viewModel: SelectionViewModel,
        ) {

            binding.selectionViewModel = viewModel
            binding.selection = item
            binding.executePendingBindings()
        }


        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        companion object {
            fun from(parent: ViewGroup, viewHolders: MutableList<DataBoundViewHolder>): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSelectionBinding.inflate(layoutInflater, parent, false)
                val viewHolder = DataBoundViewHolder(binding)
                binding.lifecycleOwner = viewHolder
                binding.categoryCard.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
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

class CategoryDiffCallback : DiffUtil.ItemCallback<SelectionItem>() {

    override fun areItemsTheSame(oldItem: SelectionItem, newItem: SelectionItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SelectionItem, newItem: SelectionItem): Boolean {
        return oldItem == newItem
    }
}



