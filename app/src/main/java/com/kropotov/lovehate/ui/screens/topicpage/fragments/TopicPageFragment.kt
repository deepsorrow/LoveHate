package com.kropotov.lovehate.ui.screens.topicpage.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentTopicPageBinding
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.screens.topicpage.TopicPageRouter
import com.kropotov.lovehate.ui.screens.topicpage.TopicPageViewModel
import com.kropotov.lovehate.data.items.TopicListItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicPageFragment @Inject constructor() :
    BaseFragment<TopicPageViewModel, FragmentTopicPageBinding>(R.layout.fragment_topic_page) {

    override val vmClass = TopicPageViewModel::class.java

    @Inject
    lateinit var router: TopicPageRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image_2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(adapterDelegate())
        binding.similarTopicsList.apply {
            this.adapter = adapter
            addItemDecoration(SpaceItemDecoration(context))
            setHasFixedSize(true)
        }
        binding.router = router

        subscribeToSimilarTopics(adapter)
    }

    @SuppressLint("NotifyDataSetChanged") // data loads only once
    private fun subscribeToSimilarTopics(adapter: ListDelegationAdapter<List<TopicListItem>>) {
        lifecycleScope.launch {
            viewModel.similarTopics.collect {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun adapterDelegate() =
        adapterDelegateViewBinding<TopicListItem, TopicListItem, ListItemTopicBinding>(
            { layoutInflater, parent ->
                ListItemTopicBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.viewModel = item
                binding.clickListener = {
                    router.navigateToNewTopic(item.id)
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
