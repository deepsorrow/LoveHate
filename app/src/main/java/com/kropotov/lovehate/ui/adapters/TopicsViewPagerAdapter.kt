package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.TopicCategory
import com.kropotov.lovehate.ui.fragments.TopicsCategoryScreenFragment
import com.kropotov.lovehate.ui.fragments.TopicsHostScreenFragment
import java.lang.IllegalArgumentException

class TopicsViewPagerAdapter(fragment: TopicsHostScreenFragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> TopicsCategoryScreenFragment.newInstance(TopicCategory.RECENT)
            1 -> TopicsCategoryScreenFragment.newInstance(TopicCategory.NEW)
            2 -> TopicsCategoryScreenFragment.newInstance(TopicCategory.POPULAR)
            3 -> TopicsCategoryScreenFragment.newInstance(TopicCategory.MOST_LOVED)
            4 -> TopicsCategoryScreenFragment.newInstance(TopicCategory.MOST_HATED)
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}