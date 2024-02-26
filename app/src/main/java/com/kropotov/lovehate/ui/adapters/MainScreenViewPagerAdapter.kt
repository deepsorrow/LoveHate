package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.ui.fragments.FeedFragment
import com.kropotov.lovehate.ui.fragments.TopicsHostScreenFragment
import java.lang.IllegalArgumentException

class MainScreenViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FeedFragment.newInstance()
            1 -> TopicsHostScreenFragment.newInstance()
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}