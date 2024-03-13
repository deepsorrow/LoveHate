package com.kropotov.lovehate.ui.fragments.topicPage

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentTopicPageBinding
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.adapters.TopicsListItemAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.viewmodels.topicPage.TopicPageViewModel
import com.kropotov.lovehate.ui.viewmodels.topics.TopicListItemVm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopicPageFragment @Inject constructor() :
    BaseFragment<TopicPageViewModel, FragmentTopicPageBinding>(
        R.layout.fragment_topic_page
    ) {

    override val vmClass = TopicPageViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val similarTopicsAdapter = ListDelegationAdapter(adapterDelegate())
        binding.similarTopicsList.apply {
            this.adapter = similarTopicsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(context, R.dimen.one_dp))
        }

        lifecycleScope.launch {
            similarTopicsAdapter.items = viewModel.getSimilarTopics()
            similarTopicsAdapter.notifyDataSetChanged()
        }

        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
        requireActivity().onBackPressedDispatcher.addCallback {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setTransparentStatusBar(true)
    }

    override fun onPause() {
        super.onPause()
        setTransparentStatusBar(false)
    }

    private fun setTransparentStatusBar(transparent: Boolean) {
        val window = requireActivity().window
        if (transparent) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.statusBarColor =
                requireContext().getColorAttr(androidx.appcompat.R.attr.colorPrimary)
        }
    }

    private fun adapterDelegate() =
        adapterDelegateViewBinding<TopicListItemVm, TopicListItemVm, ListItemTopicBinding>(
            { layoutInflater, parent ->
                ListItemTopicBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.viewModel = item
                binding.clickListener = View.OnClickListener {
                    viewModel.router.navigateToNewTopic(item.id)
                }
            }
        }

    companion object {

        const val TOPIC_PAGE_ID = "arg_topic_page_id"
        fun newInstance(id: Int) = TopicPageFragment().withArgs {
            putInt(TOPIC_PAGE_ID, id)
        }
    }
}