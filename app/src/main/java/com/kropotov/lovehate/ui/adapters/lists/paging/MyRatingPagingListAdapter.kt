package com.kropotov.lovehate.ui.adapters.lists.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.kropotov.lovehate.databinding.ListItemMyRatingBinding
import com.kropotov.lovehate.databinding.ListItemMyRatingPlaceholderBinding
import com.kropotov.lovehate.fragment.MyRatedEvent
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingListAdapter
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingViewHolder
import com.kropotov.lovehate.ui.screens.myrating.MyRatingListItemViewModel

class MyRatingPagingListAdapter
    : BasePagingListAdapter<MyRatedEvent, MyRatingPagingListAdapter.ViewHolder>(MyRatingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == PLACEHOLDER_VIEW_TYPE) {
            ListItemMyRatingPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        } else {
            ListItemMyRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }

        return ViewHolder(view)
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : BasePagingViewHolder<MyRatedEvent>(binding) {

        override fun bind(item: MyRatedEvent) {
            if (binding is ListItemMyRatingBinding) {
                with(binding) {
                    viewModel = MyRatingListItemViewModel(item)
                }
            }
        }
    }

    private class MyRatingDiffCallback : DiffUtil.ItemCallback<MyRatedEvent>() {
        override fun areItemsTheSame(
            oldItem: MyRatedEvent,
            newItem: MyRatedEvent
        ) = oldItem.sourceType == newItem.sourceType && oldItem.date == newItem.date

        override fun areContentsTheSame(
            oldItem: MyRatedEvent,
            newItem: MyRatedEvent
        ) = oldItem == newItem
    }
}