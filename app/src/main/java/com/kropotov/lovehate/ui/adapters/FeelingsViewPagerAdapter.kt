package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.OpinionType.UNION
import com.kropotov.lovehate.data.OpinionType.LOVE
import com.kropotov.lovehate.data.OpinionType.NEUTRAL
import com.kropotov.lovehate.data.OpinionType.HATE
import com.kropotov.lovehate.ui.fragments.feed.FeedFragment
import com.kropotov.lovehate.ui.fragments.feed.FeelingScreenFragment
import java.lang.IllegalArgumentException

class FeelingsViewPagerAdapter(fragment: FeedFragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FeelingScreenFragment.newInstance(UNION)
            1 -> FeelingScreenFragment.newInstance(LOVE)
            2 -> FeelingScreenFragment.newInstance(NEUTRAL)
            3 -> FeelingScreenFragment.newInstance(HATE)
            else -> throw IllegalArgumentException("Non-existent felling screen position!")
        }
}