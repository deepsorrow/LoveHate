package com.kropotov.lovehate.ui.screens.topics.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.databinding.FragmentTopicsBinding
import com.kropotov.lovehate.ui.adapters.lists.TopicsListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.screens.topics.TopicsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference
import javax.inject.Inject

class TopicsFragment : BaseFragment<TopicsViewModel, FragmentTopicsBinding>(
    R.layout.fragment_topics
) {

    override val vmClass = TopicsViewModel::class.java

    @Inject
    lateinit var adapter: TopicsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsList.apply {
            this.adapter = this@TopicsFragment.adapter
            addItemDecoration(SpaceItemDecoration(context))
        }
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        viewModel.myTopicsToolbar.arrowBackAction.set {
            val weakThis = WeakReference(parentFragmentManager)
            weakThis.get()?.popBackStack()
        }

        initShimmerLayout()
        subscribeToData()
    }

    override fun showError(message: InformMessage) {
        super.showError(message)
        hideShimmerLayout()
    }

    private fun subscribeToData() {
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
                binding.topicsList.layoutManager?.scrollToPosition(0)
            }
        })
    }

    private fun hideShimmerLayout() {
        binding.shimmerLayout.hideShimmer()
        binding.placeholderList.root.visibility = View.GONE
    }

    companion object {

        const val TOPIC_CATEGORY = "arg_topic_category"

        fun newInstance(category: TopicType) =
            TopicsFragment().withArgs {
                putSerializable(TOPIC_CATEGORY, category)
            }
    }
}
