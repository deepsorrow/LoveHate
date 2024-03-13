package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.ui.fragments.contribute.ContributeFragment
import com.kropotov.lovehate.ui.fragments.feed.FeedFragment
import com.kropotov.lovehate.ui.fragments.topics.TopicsHostFragment
import java.lang.IllegalArgumentException

const val FEED_PAGE_INDEX = 0
const val TOPICS_HOST_PAGE_INDEX = 1
const val CONTRIBUTE_PAGE_INDEX = 2
const val RATINGS_PAGE_INDEX = 3
const val PROFILE_PAGE_INDEX = 4

class MainScreenViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            FEED_PAGE_INDEX -> FeedFragment.newInstance()
            TOPICS_HOST_PAGE_INDEX -> TopicsHostFragment.newInstance()
            CONTRIBUTE_PAGE_INDEX -> ContributeFragment.newInstance()
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}