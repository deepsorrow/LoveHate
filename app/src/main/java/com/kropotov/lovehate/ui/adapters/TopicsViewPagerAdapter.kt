package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.TopicsSortType.RECENT
import com.kropotov.lovehate.data.TopicsSortType.NEW
import com.kropotov.lovehate.data.TopicsSortType.POPULAR
import com.kropotov.lovehate.data.TopicsSortType.MOST_LOVED
import com.kropotov.lovehate.data.TopicsSortType.MOST_HATED
import com.kropotov.lovehate.ui.fragments.topics.TopicsFragment
import com.kropotov.lovehate.ui.fragments.topics.TopicsHostFragment
import java.lang.IllegalArgumentException

const val TOPIC_CATEGORY_PAGE_COUNT = 5
private const val RECENT_TOPICS_PAGE_INDEX = 0
private const val NEW_TOPICS_PAGE_INDEX = 1
private const val POPULAR_TOPICS_PAGE_INDEX = 2
private const val MOST_LOVED_TOPICS_PAGE_INDEX = 3
private const val MOST_HATED_TOPICS_PAGE_INDEX = 4

class TopicsViewPagerAdapter(fragment: TopicsHostFragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TOPIC_CATEGORY_PAGE_COUNT

    override fun createFragment(position: Int): Fragment =
        when (position) {
            RECENT_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(RECENT)
            NEW_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(NEW)
            POPULAR_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(POPULAR)
            MOST_LOVED_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(MOST_LOVED)
            MOST_HATED_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(MOST_HATED)
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}