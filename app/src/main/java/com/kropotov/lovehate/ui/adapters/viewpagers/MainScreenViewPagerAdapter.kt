package com.kropotov.lovehate.ui.adapters.viewpagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment
import com.kropotov.lovehate.ui.screens.profile.fragments.ProfileFragment
import com.kropotov.lovehate.ui.screens.ratings.fragments.RatingsFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsHostFragment
import java.lang.IllegalArgumentException

const val NEW_TOPIC_PAGE_INDEX = -1
const val FEED_PAGE_INDEX = 0
const val TOPICS_HOST_PAGE_INDEX = 1
const val RATINGS_PAGE_INDEX = 2
const val PROFILE_PAGE_INDEX = 3

const val MAIN_SCREEN_PAGE_COUNT = 4

class MainScreenViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = MAIN_SCREEN_PAGE_COUNT

    override fun createFragment(position: Int): Fragment =
        when (position) {
            FEED_PAGE_INDEX -> OpinionsHostFragment.newInstance()
            TOPICS_HOST_PAGE_INDEX -> TopicsHostFragment.newInstance()
            RATINGS_PAGE_INDEX -> RatingsFragment.newInstance()
            PROFILE_PAGE_INDEX -> ProfileFragment.newInstance()
            else -> throw IllegalArgumentException("Non-existent topic screen position!")
        }
}
