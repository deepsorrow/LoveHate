package com.kropotov.lovehate.ui.screens.feed.fragments

import android.os.Bundle
import android.view.View
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentFeedBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.OpinionsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.FeedTabLayoutInitializer
import com.kropotov.lovehate.ui.screens.feed.FeedViewModel
import com.kropotov.lovehate.ui.utilities.withArgs

class FeedFragment :
    BaseFragment<FeedViewModel, FragmentFeedBinding>(R.layout.fragment_feed),
    FeedTabLayoutInitializer {

    override val vmClass = FeedViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.opinionsPagerContainer.apply {
            adapter = OpinionsViewPagerAdapter(this@FeedFragment, viewModel.topicId)

            initTabLayout(
                viewModel.toolbar,
                binding.opinionsTabLayout,
                this,
                binding.opinionsRootLayout
            )
        }
    }

    companion object {
        const val FEED_TOPIC_ID_ARG = "arg_feed_topic_id"

        fun newInstance(topicId: Int? = null) = FeedFragment().withArgs {
            topicId?.let {
                putInt(FEED_TOPIC_ID_ARG, topicId)
            }
        }
    }
}
