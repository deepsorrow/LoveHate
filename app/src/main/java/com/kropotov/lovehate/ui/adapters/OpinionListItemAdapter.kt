package com.kropotov.lovehate.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.databinding.ListItemOpinionBinding
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.ui.viewmodels.feed.OpinionViewModel

class OpinionListItemAdapter : PagingDataAdapter<OpinionListItem, OpinionListItemAdapter.ViewHolder>(
    OpinionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemOpinionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val opinion = getItem(position)
        if (opinion != null) {
            holder.bind(opinion)
        }
    }

    class ViewHolder(
        private val binding: ListItemOpinionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OpinionListItem) {
            with(binding) {
                viewModel = OpinionViewModel(item)
                executePendingBindings()
            }
        }
    }
}

private class OpinionDiffCallback : DiffUtil.ItemCallback<OpinionListItem>() {
    override fun areItemsTheSame(oldItem: OpinionListItem, newItem: OpinionListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OpinionListItem, newItem: OpinionListItem): Boolean {
        return oldItem == newItem
    }
}