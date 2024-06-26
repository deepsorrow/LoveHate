package com.kropotov.lovehate.ui.screens.topics.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.databinding.FragmentTopicsBinding
import com.kropotov.lovehate.databinding.ToolbarBinding
import com.kropotov.lovehate.ui.adapters.lists.paging.TopicsPagingListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.screens.topics.TopicsViewModel
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsHostFragment.Companion.TOPICS_SEARCH_QUERY_KEY
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicsFragment : BaseFragment<TopicsViewModel, FragmentTopicsBinding>(
    R.layout.fragment_topics
) {

    override val vmClass = TopicsViewModel::class.java

    @Inject
    lateinit var adapter: TopicsPagingListAdapter

    private var currentSearchQuery: String? = null
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.registerPagingAdapter()
        binding.topicsList.apply {
            this.adapter = this@TopicsFragment.adapter
            addItemDecoration(SpaceItemDecoration(context))
        }
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        binding.toolbarLayout.setOnInflateListener { _, inflated ->
            val binding: ToolbarBinding? = DataBindingUtil.bind(inflated)
            binding?.toolbarContract = viewModel.separateToolbar
            binding?.root?.setToolbarArrowBackAction()
        }
        if (viewModel.separateToolbar.toolbarVisibility.get()) {
            binding.toolbarLayout.viewStub?.inflate()
        }
    }

    override fun onResume() {
        super.onResume()
        searchTopics((parentFragment as? TopicsHostFragment)?.currentQuerySearch.orEmpty())
        // Only one fragmentResultListener may be registered with the same key,
        // so register inside onResume, as ViewPager resumes current fragment.
        setFragmentResultListener(TOPICS_SEARCH_QUERY_KEY) { _, bundle ->
            val query = bundle.getString(TOPICS_SEARCH_QUERY_KEY)
            searchTopics(query.orEmpty())
        }
    }

    override fun onSnackbarMessageShow(message: InformMessage) {
        hideShimmer()
    }

    private fun searchTopics(query: String) {
        currentSearchQuery == query && return
        currentSearchQuery = query

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchTopics(query).collectLatest {
                binding.refreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

    companion object {

        const val TOPIC_CATEGORY = "arg_topic_category"

        fun newInstance(category: TopicType) =
            TopicsFragment().withArgs {
                putSerializable(TOPIC_CATEGORY, category)
            }
    }
}
