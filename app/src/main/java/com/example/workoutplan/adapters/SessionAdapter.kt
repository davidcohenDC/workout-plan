package com.example.workoutplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.data.relations.SessionItem
import com.example.workoutplan.databinding.ListItemStatisticBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SessionAdapter(
    private val sessionClickListener: SessionClickListener
) : ListAdapter<Session, SessionAdapter.SessionHolder>(SessionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionAdapter.SessionHolder {
        return SessionHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SessionAdapter.SessionHolder, position: Int) {
        val workoutItem = getItem(position) as Session
        holder.bind(workoutItem,sessionClickListener)
    }

    fun customSubmitList(list: List<Session>?) {
        adapterScope.launch {
            val items = list?.map { it }?.toList()
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyItemInserted(itemCount)
            }
        }
    }

    class SessionHolder private constructor(
        private val binding: ListItemStatisticBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Session,
            sessionClickListener: SessionClickListener
        ) {
            binding.session = item
            binding.clickListener = sessionClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SessionHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStatisticBinding.inflate(layoutInflater,parent,false)
                return SessionHolder(binding)
            }
        }
    }
}

class SessionClickListener(val clickListener: (session: Session) -> Unit) {
    private var clicked = false

    fun onClick(session: Session) {
        if (clicked) return
        clicked = false
        clickListener(session)
    }
}

class SessionDiffCallback : DiffUtil.ItemCallback<Session>() {

    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.workoutId == newItem.workoutId
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem == newItem
    }

}
