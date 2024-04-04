package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.screens.topics.TopicsRouter
import com.kropotov.lovehate.ui.utilities.plusServerIp
import javax.inject.Inject

class TopicsListAdapter @Inject constructor(
    private val router: TopicsRouter
) : PagingDataAdapter<TopicListItem, TopicsListAdapter.ViewHolder>(TopicsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            router
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val opinion = getItem(position)
        if (opinion != null) {
            holder.bind(opinion)
        }
    }

    class ViewHolder(
        private val binding: ListItemTopicBinding,
        private val router: TopicsRouter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopicListItem) {
            with(binding) {
                viewModel = com.kropotov.lovehate.data.items.TopicListItem(item)
                clickListener = {
                    router.navigateToTopicPage(item.id, item.thumbnailUrl.plusServerIp())
                }
                executePendingBindings()
            }
        }
    }
}

private class TopicsDiffCallback :
    DiffUtil.ItemCallback<TopicListItem>() {
    override fun areItemsTheSame(
        oldItem: TopicListItem,
        newItem: TopicListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TopicListItem,
        newItem: TopicListItem
    ): Boolean {
        return oldItem == newItem
    }
}
