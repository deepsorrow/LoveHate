package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.ui.fragments.contribute.ContributeFragment
import com.kropotov.lovehate.ui.fragments.feed.FeedFragment
import com.kropotov.lovehate.ui.fragments.topics.TopicsHostScreenFragment
import java.lang.IllegalArgumentException

class MainScreenViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FeedFragment.newInstance()
            1 -> TopicsHostScreenFragment.newInstance()
            2 -> ContributeFragment.newInstance()
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}