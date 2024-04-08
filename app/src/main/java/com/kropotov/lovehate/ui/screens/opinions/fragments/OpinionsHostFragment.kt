package com.kropotov.lovehate.ui.screens.opinions.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentOpinionsHostBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.OpinionsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.TabLayoutInitializer
import com.kropotov.lovehate.ui.screens.opinions.OpinionsHostViewModel
import com.kropotov.lovehate.ui.utilities.reduceDragSensitivity
import com.kropotov.lovehate.ui.utilities.withArgs
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class OpinionsHostFragment :
    BaseFragment<OpinionsHostViewModel, FragmentOpinionsHostBinding>(R.layout.fragment_opinions_host),
    TabLayoutInitializer {

    override val vmClass = OpinionsHostViewModel::class.java

    val currentQuerySearch get() = viewModel.searchQuery.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.opinionsPagerContainer.apply {
            adapter = OpinionsViewPagerAdapter(this@OpinionsHostFragment, viewModel.topicId)
            reduceDragSensitivity()

            initTabLayout(
                toolbarContract = viewModel.toolbar,
                tabLayout = binding.opinionsTabLayout,
                viewPager = this,
                container = binding.opinionsRootLayout
            )
        }

        startDispatchingSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun startDispatchingSearchQuery() {
        lifecycleScope.launch {
            viewModel.searchQuery.debounce(DEBOUNCE_TIME_MS).collect { query ->
                val bundle = bundleOf(OPINIONS_SEARCH_QUERY_KEY to query)
                childFragmentManager.setFragmentResult(OPINIONS_SEARCH_QUERY_KEY, bundle)
            }
        }
    }

    companion object {
        const val OPINIONS_SEARCH_QUERY_KEY = "opinions_search_query_key"
        const val FEED_TOPIC_ID_ARG = "arg_feed_topic_id"

        fun newInstance(topicId: Int? = null) = OpinionsHostFragment().withArgs {
            topicId?.let {
                putInt(FEED_TOPIC_ID_ARG, topicId)
            }
        }
    }
}
