package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.OpinionSortType.UNION
import com.kropotov.lovehate.data.OpinionSortType.LOVE
import com.kropotov.lovehate.data.OpinionSortType.INDIFFERENCE
import com.kropotov.lovehate.data.OpinionSortType.HATE
import com.kropotov.lovehate.ui.fragments.feed.FeedFragment
import com.kropotov.lovehate.ui.fragments.OpinionsFragment
import java.lang.IllegalArgumentException

private const val UNION_OPINIONS_PAGE_INDEX = 0
private const val LOVE_OPINIONS_PAGE_INDEX = 1
private const val INDIFFERENT_OPINIONS_PAGE_INDEX = 2
private const val HATE_OPINIONS_PAGE_INDEX = 3

class OpinionsViewPagerAdapter(fragment: FeedFragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment =
        when (position) {
            UNION_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstance(UNION)
            LOVE_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstance(LOVE)
            INDIFFERENT_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstance(INDIFFERENCE)
            HATE_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstance(HATE)
            else -> throw IllegalArgumentException("Non-existent opinions screen position!")
        }
}