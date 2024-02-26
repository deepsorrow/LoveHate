package com.kropotov.lovehate.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kropotov.lovehate.data.FeelingType.UNION
import com.kropotov.lovehate.data.FeelingType.LOVE
import com.kropotov.lovehate.data.FeelingType.NEUTRAL
import com.kropotov.lovehate.data.FeelingType.HATE
import com.kropotov.lovehate.ui.fragments.FeedFragment
import com.kropotov.lovehate.ui.fragments.FeelingScreenFragment
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