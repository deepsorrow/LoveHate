package com.kropotov.lovehate.ui.adapters.lists.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.kropotov.lovehate.databinding.ListItemOpinionBinding
import com.kropotov.lovehate.databinding.ListItemOpinionPlaceholderBinding
import com.kropotov.lovehate.fragment.OpinionListItem
import com.kropotov.lovehate.ui.adapters.lists.OpinionAttachmentsListAdapter
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingListAdapter
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingViewHolder
import com.kropotov.lovehate.ui.screens.opinions.OpinionListItemViewModel
import com.kropotov.lovehate.ui.screens.opinions.OpinionsRouter
import com.kropotov.lovehate.ui.screens.opinions.OpinionsViewModel
import com.kropotov.lovehate.ui.utilities.plusServerIp

class OpinionsPagingListAdapter(
    private val router: OpinionsRouter,
    private val opinionsViewModel: OpinionsViewModel
) : BasePagingListAdapter<OpinionListItem, OpinionsPagingListAdapter.ViewHolder>(
    OpinionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == PLACEHOLDER_VIEW_TYPE) {
            ListItemOpinionPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        } else {
            ListItemOpinionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }

        return ViewHolder(view)
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : BasePagingViewHolder<OpinionListItem>(binding) {

        override fun bind(item: OpinionListItem) {
            if (binding is ListItemOpinionBinding) {
                with(binding) {
                    viewModel = OpinionListItemViewModel(item, opinionsViewModel)
                    binding.bodyText.originalText = item.text
                    if (binding.bodyText.expanded) {
                        binding.bodyText.toggle()
                    }

                    attachmentsList.apply {
                        val items = item.attachmentUrls.map { it.plusServerIp() }

                        adapter = OpinionAttachmentsListAdapter(router, items)
                        setHasFixedSize(true)
                    }
                }
            }
        }
    }
}

private class OpinionDiffCallback : DiffUtil.ItemCallback<OpinionListItem>() {
    override fun areItemsTheSame(
        oldItem: OpinionListItem,
        newItem: OpinionListItem
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: OpinionListItem,
        newItem: OpinionListItem
    ) = oldItem == newItem
}

