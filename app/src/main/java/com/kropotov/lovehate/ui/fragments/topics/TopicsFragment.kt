package com.kropotov.lovehate.ui.fragments.topics

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicsSortType
import com.kropotov.lovehate.databinding.FragmentTopicsBinding
import com.kropotov.lovehate.ui.adapters.TopicsListItemAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.viewmodels.topics.TopicCategoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicsFragment : BaseFragment<TopicCategoryViewModel, FragmentTopicsBinding>(
    R.layout.fragment_topics
) {

    override val vmClass = TopicCategoryViewModel::class.java

    @Inject
    lateinit var adapter: TopicsListItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topicsRecyclerView.apply {
            this.adapter = this@TopicsFragment.adapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(context, R.dimen.one_dp))
        }

        lifecycleScope.launch {
            viewModel.searchTopics("").collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {

        const val TOPIC_CATEGORY = "arg_topic_category"

        fun newInstance(category: TopicsSortType) =
            TopicsFragment().withArgs {
                putSerializable(TOPIC_CATEGORY, category)
            }
    }
}