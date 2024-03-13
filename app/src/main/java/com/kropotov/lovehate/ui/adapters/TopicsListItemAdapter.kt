package com.kropotov.lovehate.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment
import com.kropotov.lovehate.ui.viewmodels.topics.TopicListItemVm
import com.kropotov.lovehate.ui.viewmodels.topics.TopicsRouter
import javax.inject.Inject

class TopicsListItemAdapter @Inject constructor(
    val router: TopicsRouter
) : PagingDataAdapter<TopicListItem, TopicsListItemAdapter.ViewHolder>(
    TopicsDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            router
        )
    }

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
                viewModel = TopicListItemVm(item)
                clickListener = View.OnClickListener {
                    router.navigateToNewTopic(item.id)
//                    fragmentManager.commit {
//                        setCustomAnimations(
//                            R.anim.slide_up,
//                            R.anim.slide_down,
//                            0,
//                            R.anim.slide_down
//                        )
//                        add(R.id.overlay_container, TopicPageFragment.newInstance(item.id))
//                        addToBackStack(null)
//                    }
                }
                executePendingBindings()
            }
        }
    }
}

private class TopicsDiffCallback : DiffUtil.ItemCallback<TopicListItem>() {
    override fun areItemsTheSame(oldItem: TopicListItem, newItem: TopicListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TopicListItem, newItem: TopicListItem): Boolean {
        return oldItem == newItem
    }
}