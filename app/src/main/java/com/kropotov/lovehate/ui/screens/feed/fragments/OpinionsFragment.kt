package com.kropotov.lovehate.ui.screens.feed.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.databinding.FragmentOpinionsBinding
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.adapters.lists.OpinionsListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.screens.feed.OpinionsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference

class OpinionsFragment : BaseFragment<OpinionsViewModel, FragmentOpinionsBinding>(
    R.layout.fragment_opinions
) {

    override val vmClass = OpinionsViewModel::class.java

    private val adapter by lazy { OpinionsListAdapter(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.opinionsList.apply {
            adapter = this@OpinionsFragment.adapter
            addItemDecoration(SpaceItemDecoration(context))
        }
        viewModel.separateToolbar.arrowBackAction.set {
            val weakThis = WeakReference(parentFragmentManager)
            weakThis.get()?.popBackStack()
        }

        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        initShimmerLayout()
        subscribeToListData()
    }

    override fun showError(message: InformMessage) {
        super.showError(message)
        hideShimmerLayout()
    }

    private fun subscribeToListData() {
        viewModel.items
            .onEach {
                binding.refreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
            .launchIn(lifecycleScope)

        adapter.addLoadStateListener { loadState ->
            loadState.refresh.extractAndShowError()
            loadState.append.extractAndShowError()
            loadState.prepend.extractAndShowError()
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
