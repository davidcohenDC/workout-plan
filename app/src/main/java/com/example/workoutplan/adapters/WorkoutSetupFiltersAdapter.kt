//package com.example.workoutplan.adapters
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.view.animation.AnimationUtils
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.workoutplan.R
//import com.example.workoutplan.data.category.Category
//import com.example.workoutplan.data.difficulty.Difficulty
//import com.example.workoutplan.databinding.ListItemFilterBinding
//import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_CATEGORY
//import com.example.workoutplan.utilities.ITEM_VIEW_TYPE_DIFFICULTY
//import com.example.workoutplan.utilities.vibratePhone
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//class WorkoutSetupFiltersAdapter(
//        private val clickSelectListener: FilterSelectListener,
//) : ListAdapter<FilterItem, RecyclerView.ViewHolder>(FilterDiffCallBack()){
//
//    private val adapterScope = CoroutineScope(Dispatchers.Default)
//
//    /**
//     * Facccio l'infalte della view (attualmente sono uguali)
//     */
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when(viewType) {
//            ITEM_VIEW_TYPE_CATEGORY -> ViewHolder.from(parent)
//            ITEM_VIEW_TYPE_DIFFICULTY -> ViewHolder.from(parent)
//            else -> throw ClassCastException("Unknown viewType $viewType")
//        }
//    }
//
//
//    /**
//     * Seleziono il tipo di filtro
//     */
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when(holder) {
//            is ViewHolder -> {
//                val filter = getItem(position) as FilterItem
//                when(filter.type) {
//                    ITEM_VIEW_TYPE_DIFFICULTY -> {
//                        val items = getItem(position) as FilterItem.DifficultyItem
//                        holder.bindDifficulty(items, clickSelectListener)
//                    }
//                    ITEM_VIEW_TYPE_CATEGORY -> {
//                        val items = getItem(position)as FilterItem.CategoryItem
//                        holder.bindCategory(items, clickSelectListener)
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * In base al tipo di item seleziono quale tipo è per settare il bind
//     */
//    override fun getItemViewType(position: Int): Int {
//        return when(getItem(position)) {
//            is FilterItem.DifficultyItem -> ITEM_VIEW_TYPE_DIFFICULTY
//            is FilterItem.CategoryItem -> ITEM_VIEW_TYPE_CATEGORY
//        }
//    }
//
//    fun categorySubmitList(list: List<Category>?) {
//        adapterScope.launch {
//            val items = when(list) {
//                null -> listOf(DataItem.Header)
//                else -> list.map { FilterItem.CategoryItem(it) }
//            }
//        }
//    }
//
//    fun difficultySubmitList(list: List<Difficulty>?) {
//        adapterScope.launch {
//            val items = when(list) {
//                null -> listOf(DataItem.Header)
//                else -> list.map { FilterItem.DifficultyItem(it) }
//            }
//        }
//    }
//
//    class ViewHolder private constructor(
//            private val binding: ListItemFilterBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bindDifficulty(
//                item: FilterItem.DifficultyItem,
//                clickSelectListener: FilterSelectListener,
//        ) {
//            binding.filter = item
//            binding.filterSelectListener = clickSelectListener
//            binding.executePendingBindings()
//            binding.card.setOnClickListener {
//                binding.card.isChecked = !binding.card.isChecked
//            }
//        }
//
//        fun bindCategory(
//                item: FilterItem.CategoryItem,
//                clickSelectListener: FilterSelectListener,
//        ) {
//            binding.filter = item
//            binding.filterSelectListener = clickSelectListener
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListItemFilterBinding.inflate(layoutInflater, parent, false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//}
//
//class FilterDiffCallBack: DiffUtil.ItemCallback<FilterItem>() {
//
//    override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
//        return oldItem == newItem
//    }
//
//}
//
//
///**
// * In base al filtro seleziono il listner di destinazione
// */
//class FilterSelectListener(val clicklistenerCategory: (category: Category) -> Unit, val clicklistenerDifficulty: (difficulty: Difficulty) -> Unit) {
//
//    fun onClick(filter: FilterItem) {
//        when(filter) {
//            is FilterItem.CategoryItem -> clicklistenerCategory(filter.category)
//            is FilterItem.DifficultyItem -> clicklistenerDifficulty(filter.difficulty)
//        }
//    }
//}
//
//
//sealed class FilterItem {
//    abstract val type: Int
//    abstract val id: Int
//
//    data class CategoryItem(val category: Category): FilterItem() {
//        override val type: Int
//            get() = ITEM_VIEW_TYPE_CATEGORY
//        override val id: Int
//            get() = category.categoryId
//    }
//
//    data class DifficultyItem(val difficulty: Difficulty): FilterItem() {
//        override val type: Int
//            get() = ITEM_VIEW_TYPE_DIFFICULTY
//        override val id: Int
//            get() = difficulty.difficultyId
//
//    }
//}