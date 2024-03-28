package com.kropotov.lovehate.ui.screens.topics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.databinding.FragmentTopicsHostBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.TopicsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.getDrawableRes
import com.kropotov.lovehate.ui.screens.topics.TopicsHostViewModel

class TopicsHostFragment : BaseFragment<TopicsHostViewModel, FragmentTopicsHostBinding>(R.layout.fragment_topics_host) {

    override val vmClass = TopicsHostViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsPagerContainer.apply {
            adapter = TopicsViewPagerAdapter(this@TopicsHostFragment)
        }

        setTabLayout()
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.topicsTabLayout, binding.topicsPagerContainer) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.list_item_tab, null, false)
            val textView = tabView.findViewById<TextView>(R.id.tab_text)

            textView.text = resources.getString(TopicType.entries[position].title)
            if (position == 0) { // UNION
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                textView.typeface = iconsFont
                tab.view.background = resources.getDrawableRes(R.drawable.round_primary_tab)
            }

            tab.setCustomView(tabView)
        }.attach()
    }

    companion object {
        fun newInstance() = TopicsHostFragment()
    }
}
