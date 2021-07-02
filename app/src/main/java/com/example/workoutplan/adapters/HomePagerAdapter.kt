package com.example.workoutplan.adapters

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(manager: Fragment) : FragmentStateAdapter(manager) {

    private val fragmentList: MutableMap<Int, Pair<Fragment, Pair<String, Drawable>>> = mutableMapOf()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]!!.first
    }

    fun addFragment(fragment: Fragment, title: String, drawable: Drawable) {
        fragmentList[fragmentList.size] = Pair(fragment, Pair(title, drawable))
    }

    fun getPageTitle(position: Int): String {
        return fragmentList[position]!!.second.first
    }

    fun getPageIcon(position: Int): Drawable {
        return fragmentList[position]!!.second.second
    }


}