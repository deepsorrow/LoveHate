package com.kropotov.lovehate.ui.adapters.lists.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.databinding.ListItemTopicPlaceholderBinding
import com.kropotov.lovehate.fragment.TopicListItem as GeneratedTopicListItem
import com.kropotov.lovehate.data.items.TopicListItem
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingListAdapter
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingViewHolder
import com.kropotov.lovehate.ui.screens.topics.TopicsRouter
import com.kropotov.lovehate.ui.utilities.plusServerIp
import javax.inject.Inject

class TopicsPagingListAdapter @Inject constructor(
    private val router: TopicsRouter
) : BasePagingListAdapter<GeneratedTopicListItem, TopicsPagingListAdapter.ViewHolder>(
    TopicsDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == PLACEHOLDER_VIEW_TYPE) {
            ListItemTopicPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        } else {
            ListItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }

        return ViewHolder(view)
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : BasePagingViewHolder<GeneratedTopicListItem>(binding) {

        override fun bind(item: GeneratedTopicListItem) {
            if (binding is ListItemTopicBinding) {
                with(binding) {
                    viewModel = TopicListItem(item)
                    clickListener = {
                        router.navigateToTopicPage(item.id, item.thumbnailUrl.plusServerIp())
                    }
                    executePendingBindings()
                }
            }
        }
    }
}


private class TopicsDiffCallback :
    DiffUtil.ItemCallback<GeneratedTopicListItem>() {
    override fun areItemsTheSame(
        oldItem: GeneratedTopicListItem,
        newItem: GeneratedTopicListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GeneratedTopicListItem,
        newItem: GeneratedTopicListItem
    ): Boolean {
        return oldItem == newItem
    }
}