package com.kropotov.lovehate.ui.adapters.viewpagers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.screens.favorites.FavoritesFragment
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import java.lang.IllegalArgumentException

private const val UNION_TOPICS_PAGE_INDEX = 0
private const val UNION_OPINIONS_PAGE_INDEX = 1

class FavoritesViewPagerAdapter(
    fragment: FavoritesFragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            UNION_TOPICS_PAGE_INDEX -> TopicsFragment.newInstance(TopicType.FAVORITES)
            UNION_OPINIONS_PAGE_INDEX -> OpinionsFragment.newInstance(OpinionsListType.BY_FAVORITES)
            else -> throw IllegalArgumentException("Non-existent favorites screen position!")
        }
}
