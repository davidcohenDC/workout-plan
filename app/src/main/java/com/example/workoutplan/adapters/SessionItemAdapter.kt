package com.example.workoutplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplan.R
import com.example.workoutplan.adapters.items.SessionItem
import com.example.workoutplan.databinding.ListItemSessionSetBinding
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionItemAdapter(
    private val clickListener: SessionItemListener,
): ListAdapter<SessionItem, SessionItemAdapter.SessionItemHolder>(SessionItemDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionItemHolder {
        return SessionItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SessionItemHolder, position: Int) {
        val item = getItem(position) as SessionItem
        holder.bind(item, clickListener)
    }

    fun customSubmitList(list: List<SessionItem>?) {
        adapterScope.launch {
            val items = list?.map { it }?.toList()
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyItemInserted(itemCount)
            }
        }
    }

     class SessionItemHolder private constructor(
         private val binding: ListItemSessionSetBinding
         ) : RecyclerView.ViewHolder(binding.root) {
             fun bind(
                 item: SessionItem,
                 clickListener: SessionItemListener
             ) {
                 binding.clickListener = clickListener
                 binding.sessionItem = item
                 binding.executePendingBindings()

                 //if the item status is DOING ill start the rotate animation
                 if(item.status == SessionItem.Companion.STATUS.DOING) {
                     binding.boxSession.startAnimation(AnimationUtils.loadAnimation(binding.boxSession.context, R.anim.infinite_rotation))
                 }

             }

        companion object {
            fun from(parent: ViewGroup): SessionItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSessionSetBinding.inflate(layoutInflater, parent, false)
                return SessionItemHolder(binding)
            }
        }
     }

}


class SessionItemListener(val clickListener: (sessionItem: SessionItem) -> Unit) {
    private var clicked = false

    fun onClick(sessionItem: SessionItem) {
        if(clicked) return
        clicked = false
        clickListener(sessionItem)
    }

}


class SessionItemDiffCallback: DiffUtil.ItemCallback<SessionItem>() {
    override fun areItemsTheSame(oldItem: SessionItem, newItem: SessionItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SessionItem, newItem: SessionItem): Boolean {
        return oldItem == newItem
    }
}