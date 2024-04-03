package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.databinding.ListItemOpinionBinding
import com.kropotov.lovehate.fragment.OpinionListItem as GeneratedOpinionListItem
import com.kropotov.lovehate.ui.screens.opinions.OpinionListItemViewModel
import com.kropotov.lovehate.ui.screens.opinions.OpinionsRouter
import com.kropotov.lovehate.ui.screens.opinions.OpinionsViewModel
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.plusServerIp

class OpinionsListAdapter(
    private val router: OpinionsRouter,
    private val opinionsViewModel: OpinionsViewModel
) : PagingDataAdapter<GeneratedOpinionListItem, OpinionsListAdapter.ViewHolder>(
    OpinionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemOpinionBinding.inflate(
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

    inner class ViewHolder(
        private val binding: ListItemOpinionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GeneratedOpinionListItem) {
            with(binding) {
                viewModel = OpinionListItemViewModel(item, opinionsViewModel)

                if (item.attachmentUrls.isNotEmpty()) {
                    attachmentsList.apply {
                        val items = item.attachmentUrls.map { it.plusServerIp() }

                        adapter = OpinionAttachmentsListAdapter(router, items)
                        addItemDecoration(SpaceItemDecoration(binding.root.context))
                        setHasFixedSize(true)
                    }
                }
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
