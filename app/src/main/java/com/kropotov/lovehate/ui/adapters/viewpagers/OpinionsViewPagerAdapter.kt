package com.kropotov.lovehate.ui.adapters.viewpagers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.OpinionType.UNION
import com.kropotov.lovehate.data.OpinionType.LOVE
import com.kropotov.lovehate.data.OpinionType.INDIFFERENCE
import com.kropotov.lovehate.data.OpinionType.HATE
import com.kropotov.lovehate.ui.screens.feed.fragments.FeedFragment
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment
import java.lang.IllegalArgumentException

private const val UNION_OPINIONS_PAGE_INDEX = 0
private const val LOVE_OPINIONS_PAGE_INDEX = 1
private const val INDIFFERENT_OPINIONS_PAGE_INDEX = 2
private const val HATE_OPINIONS_PAGE_INDEX = 3

class OpinionsViewPagerAdapter(
    fragment: FeedFragment,
    private val topicId: Int?
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment =
        when (position) {
            UNION_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstanceFeed(UNION, topicId)
            LOVE_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstanceFeed(LOVE, topicId)
            INDIFFERENT_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstanceFeed(INDIFFERENCE, topicId)
            HATE_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstanceFeed(HATE, topicId)
            else -> throw IllegalArgumentException("Non-existent opinions screen position!")
        }
}
