package com.kropotov.lovehate.ui.fragments.topics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicsSortType
import com.kropotov.lovehate.databinding.FragmentTopicsHostBinding
import com.kropotov.lovehate.ui.adapters.TOPIC_CATEGORY_PAGE_COUNT
import com.kropotov.lovehate.ui.adapters.TopicsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.getDrawableRes
import com.kropotov.lovehate.ui.viewmodels.topics.TopicsViewModel

class TopicsHostFragment : BaseFragment<TopicsViewModel, FragmentTopicsHostBinding>(R.layout.fragment_topics_host) {

    override val vmClass = TopicsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsPagerContainer.apply {
            adapter = TopicsViewPagerAdapter(this@TopicsHostFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            }
        }

        setTabLayout()
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.topicsTabLayout, binding.topicsPagerContainer) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.list_item_tab, null, false)
            val textView = tabView.findViewById<TextView>(R.id.tab_text)

            textView.text = resources.getString(TopicsSortType.entries[position].title)
            if (position == 0) { // UNION
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                textView.typeface = iconsFont
                tab.view.background = resources.getDrawableRes(R.drawable.tab_layout_background)
            }

            tab.setCustomView(tabView)
        }.attach()
    }

    companion object {
        val tag: String = TopicsHostFragment::class.java.simpleName

        fun newInstance() = TopicsHostFragment()
    }
}