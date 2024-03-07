package com.kropotov.lovehate.ui.fragments.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicCategory
import com.kropotov.lovehate.databinding.FragmentTopicsHostScreenBinding
import com.kropotov.lovehate.ui.adapters.TopicsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utils.getDrawableRes
import com.kropotov.lovehate.ui.vm.topics.TopicsScreenVm

class TopicsHostScreenFragment : BaseFragment<TopicsScreenVm, FragmentTopicsHostScreenBinding>(R.layout.fragment_topics_host_screen) {

    override val vmClass = TopicsScreenVm::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsPagerContainer.apply {
            adapter = TopicsViewPagerAdapter(this@TopicsHostScreenFragment)
            offscreenPageLimit = 5 // To load all fragments in advance
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

            textView.text = resources.getString(TopicCategory.entries[position].title)
            if (position == 0) { // UNION
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                textView.typeface = iconsFont
                tab.view.background = resources.getDrawableRes(R.drawable.tab_layout_background)
            }

            tab.setCustomView(tabView)
        }.attach()
    }

    companion object {
        val tag: String = TopicsHostScreenFragment::class.java.simpleName

        fun newInstance() = TopicsHostScreenFragment()
    }
}