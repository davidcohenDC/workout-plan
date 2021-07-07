package com.example.workoutplan.adapters.items

import com.example.workoutplan.data.entity.Category
import com.example.workoutplan.data.entity.Difficulty
import com.example.workoutplan.data.entity.Muscle

sealed class SelectionItem {
    abstract val id: Long

    data class CategoryItem(val category: Category) : SelectionItem() {
        override val id: Long
            get() = category.categoryId
    }

    data class DifficultyItem(val difficulty: Difficulty) : SelectionItem() {
        override val id: Long
            get() = difficulty.difficultyId
    }

    data class MuscleItem(val muscle: Muscle) : SelectionItem() {
        override val id: Long
            get() = muscle.muscleId
    }

}