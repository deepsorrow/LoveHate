package com.kropotov.lovehate.ui.screens.topics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.databinding.FragmentTopicsHostBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.TopicsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.topics.TopicsHostViewModel
import com.kropotov.lovehate.ui.utilities.TabLayoutInitializer.Companion.addOnTabCustomSelectedListener
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.reduceDragSensitivity
import com.google.android.material.R as RMaterial
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class TopicsHostFragment : BaseFragment<TopicsHostViewModel, FragmentTopicsHostBinding>(R.layout.fragment_topics_host) {

    override val vmClass = TopicsHostViewModel::class.java

    val currentQuerySearch get() = viewModel.searchQuery.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.apply {
            adapter = TopicsViewPagerAdapter(this@TopicsHostFragment)
            reduceDragSensitivity()
        }

        setTabLayout()
        startDispatchingSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun startDispatchingSearchQuery() {
        lifecycleScope.launch {
            viewModel.searchQuery.debounce(DEBOUNCE_TIME_MS).collect { query ->
                val bundle = bundleOf(TOPICS_SEARCH_QUERY_KEY to query)
                childFragmentManager.setFragmentResult(TOPICS_SEARCH_QUERY_KEY, bundle)
            }
        }
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.list_item_tab, null, false)
            val textView = tabView.findViewById<TextView>(R.id.tab_text)
            textView.text = resources.getString(TopicType.entries[position].title)
            if (position == 0) {
                val textColor = requireContext().getColorAttr(RMaterial.attr.colorOnPrimary)
                textView.setTextColor(textColor)
            }

            tab.setCustomView(tabView)
        }.attach()

        binding.tabLayout.addOnTabCustomSelectedListener()
    }

    companion object {
        const val TOPICS_SEARCH_QUERY_KEY = "search_query_key"
        fun newInstance() = TopicsHostFragment()
    }
}
