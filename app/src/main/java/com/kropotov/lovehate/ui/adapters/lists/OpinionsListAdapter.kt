package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.databinding.ListItemOpinionBinding
import com.kropotov.lovehate.fragment.OpinionListItem as GeneratedOpinionListItem
import com.kropotov.lovehate.ui.screens.feed.OpinionListItem
import com.kropotov.lovehate.ui.screens.feed.OpinionsViewModel

class OpinionsListAdapter(
    private val opinionsViewModel: OpinionsViewModel
) : PagingDataAdapter<GeneratedOpinionListItem, OpinionsListAdapter.ViewHolder>(
    OpinionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            opinionsViewModel, ListItemOpinionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val opinion = getItem(position)
        if (opinion != null) {
            holder.bind(opinion)
        }
    }

    class ViewHolder(
        private val opinionsViewModel: OpinionsViewModel,
        private val binding: ListItemOpinionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GeneratedOpinionListItem) {
            with(binding) {
                viewModel =
                    OpinionListItem(item, opinionsViewModel)
                executePendingBindings()
            }
        }
    }
}

private class OpinionDiffCallback : DiffUtil.ItemCallback<GeneratedOpinionListItem>() {
    override fun areItemsTheSame(
        oldItem: GeneratedOpinionListItem,
        newItem: GeneratedOpinionListItem
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: GeneratedOpinionListItem,
        newItem: GeneratedOpinionListItem
    ) = oldItem == newItem
}
