package com.kropotov.lovehate.ui.screens.topicpage.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
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
import com.kropotov.lovehate.ui.adapters.lists.ImagesCarouselAdapter
import com.kropotov.lovehate.ui.utilities.plusServerIp
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopicPageFragment @Inject constructor() :
    BaseFragment<TopicPageViewModel, FragmentTopicPageBinding>(R.layout.fragment_topic_page) {

    override val vmClass = TopicPageViewModel::class.java

    @Inject
    lateinit var router: TopicPageRouter

    private val firstImageUrl by lazy { arguments?.getString(IMAGE_URL_KEY).orEmpty() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagesCarouselAdapter = ImagesCarouselAdapter(mutableListOf(firstImageUrl))
        binding.carouselList.apply {
            this.adapter = imagesCarouselAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.carouselIndicator.attachToRecyclerView(this)
            PagerSnapHelper().attachToRecyclerView(this)
        }

        val similarTopicsAdapter = ListDelegationAdapter(adapterDelegate())
        binding.similarTopicsList.apply {
            this.adapter = similarTopicsAdapter
            addItemDecoration(SpaceItemDecoration(context))
            setHasFixedSize(true)
        }
        binding.router = router

        subscribeToSimilarTopics(similarTopicsAdapter)
        subscribeToCarouselImages(imagesCarouselAdapter)
        subscribeToNewOpinionCreated()
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

    @SuppressLint("NotifyDataSetChanged") // data loads only once
    private fun subscribeToCarouselImages(adapter: ImagesCarouselAdapter) {
        lifecycleScope.launch {
            viewModel.carouselImages.collect {
                adapter.setItems(it)
            }
        }
    }

    private fun subscribeToNewOpinionCreated() {
        childFragmentManager.setFragmentResultListener(NAVIGATE_TO_OPINIONS_EVENT, this) { _, _ ->
            router.showOpinions()
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
                    router.navigateToSimilarTopic(item.id, item.thumbnailUrl.plusServerIp())
                }
                binding.executePendingBindings()
            }
        }

    companion object {

        const val TOPIC_PAGE_ID = "arg_topic_page_id"
        const val NAVIGATE_TO_OPINIONS_EVENT = "navigate_to_opinions_event"
        const val IMAGE_URL_KEY = "image_url_key"
        fun newInstance(id: Int, thumbnailUrl: String? = null) =
            TopicPageFragment().withArgs {
                putInt(TOPIC_PAGE_ID, id)
                thumbnailUrl?.let { putString(IMAGE_URL_KEY, thumbnailUrl) }
            }
    }
}
