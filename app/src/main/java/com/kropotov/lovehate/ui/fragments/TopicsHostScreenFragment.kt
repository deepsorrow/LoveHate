package com.kropotov.lovehate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicCategory
import com.kropotov.lovehate.databinding.FragmentTopicsHostScreenBinding
import com.kropotov.lovehate.ui.adapters.TopicsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.vm.ToolbarVm
import com.kropotov.lovehate.ui.vm.TopicsScreenVm

class TopicsHostScreenFragment : BaseFragment<TopicsScreenVm, FragmentTopicsHostScreenBinding>(R.layout.fragment_topics_host_screen) {

    override val vmClass = TopicsScreenVm::class.java

    val toolbarVm: ToolbarVm by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsPagerContainer.apply {
            adapter = TopicsViewPagerAdapter(this@TopicsHostScreenFragment)
            offscreenPageLimit = 5 // To load all fragments in advance
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
                tab.view.background = ResourcesCompat.getDrawable(resources, R.drawable.tab_layout_background, null)
            }

            tab.setCustomView(tabView)
        }.attach()
    }

    companion object {
        val tag: String = TopicsHostScreenFragment::class.java.simpleName

        fun newInstance() = TopicsHostScreenFragment()
    }
}