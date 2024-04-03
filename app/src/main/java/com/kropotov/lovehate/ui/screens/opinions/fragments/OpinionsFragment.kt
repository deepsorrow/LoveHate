package com.kropotov.lovehate.ui.screens.opinions.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.databinding.FragmentOpinionsBinding
import com.kropotov.lovehate.databinding.ToolbarBinding
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.adapters.lists.OpinionsListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.opinions.OpinionsRouter
import com.kropotov.lovehate.ui.screens.opinions.OpinionsViewModel
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment.Companion.OPINIONS_SEARCH_QUERY_KEY
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class OpinionsFragment : BaseFragment<OpinionsViewModel, FragmentOpinionsBinding>(
    R.layout.fragment_opinions
) {

    override val vmClass = OpinionsViewModel::class.java

    @Inject
    lateinit var router: OpinionsRouter

    private val adapter by lazy { OpinionsListAdapter(router, viewModel) }
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.opinionsList.apply {
            adapter = this@OpinionsFragment.adapter
            addItemDecoration(SpaceItemDecoration(context))
        }
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }

        initShimmerLayout()
        adapter.addLoadStateListener { loadState ->
            loadState.refresh.extractAndShowError()
            loadState.append.extractAndShowError()
            loadState.prepend.extractAndShowError()
        }

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
        searchOpinions((parentFragment as? OpinionsHostFragment)?.currentQuerySearch.orEmpty())
        /** Only one fragmentResultListener may be registered with the same key,
         so register inside onResume, as ViewPager resumes current fragment. **/
        setFragmentResultListener(OPINIONS_SEARCH_QUERY_KEY) { _, bundle ->
            val query = bundle.getString(OPINIONS_SEARCH_QUERY_KEY)
            searchOpinions(query.orEmpty())
        }

    }

    override fun onSnackbarMessageShow() {
        hideShimmerLayout()
    }

    private fun searchOpinions(query: String) {
        viewModel.currentSearchQuery == query && return

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchOpinions(query).collectLatest {
                binding.refreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

    private fun initShimmerLayout() {
        binding.shimmerLayout.showShimmer(true)
        adapter.addOnPagesUpdatedListener { hideShimmerLayout() }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.opinionsList.layoutManager?.scrollToPosition(0)
            }
        })
    }

    private fun hideShimmerLayout() {
        binding.shimmerLayout.hideShimmer()
        binding.placeholderList.root.visibility = View.GONE
    }

    companion object {

        const val OPINIONS_TOPIC_ID_ARG = "arg_opinion_topic_id"
        const val OPINIONS_SORT_TYPE_ARG = "arg_opinion_sort_type"
        const val OPINIONS_FILTER_TYPE_ARG = "arg_opinion_filter_type"

        fun newInstanceFeed(
            type: OpinionType,
            topicId: Int?
        ) = OpinionsFragment().withArgs {
            putSerializable(OPINIONS_SORT_TYPE_ARG, type)
            topicId?.let { putInt(OPINIONS_TOPIC_ID_ARG, it) }
        }

        fun newInstance(
            listType: OpinionsListType
        ) = OpinionsFragment().withArgs {
                putSerializable(OPINIONS_SORT_TYPE_ARG, OpinionType.UNION)
                putSerializable(OPINIONS_FILTER_TYPE_ARG, listType)
            }
    }
}