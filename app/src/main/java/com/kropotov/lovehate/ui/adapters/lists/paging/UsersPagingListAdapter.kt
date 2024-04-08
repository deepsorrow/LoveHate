package com.kropotov.lovehate.ui.adapters.lists.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.kropotov.lovehate.databinding.ListItemUserBinding
import com.kropotov.lovehate.fragment.UserListItem as GeneratedUserListItem
import com.kropotov.lovehate.data.items.UserListItem
import com.kropotov.lovehate.databinding.ListItemUserPlaceholderBinding
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingListAdapter
import com.kropotov.lovehate.ui.adapters.lists.paging.base.BasePagingViewHolder
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import javax.inject.Inject

class UsersPagingListAdapter @Inject constructor(val resourceProvider: ResourceProvider) :
    BasePagingListAdapter<GeneratedUserListItem, UsersPagingListAdapter.ViewHolder>(
        UsersDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == PLACEHOLDER_VIEW_TYPE) {
            ListItemUserPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        } else {
            ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }

        return ViewHolder(view)
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : BasePagingViewHolder<GeneratedUserListItem>(binding) {

        override fun bind(item: GeneratedUserListItem) {
            if (binding is ListItemUserBinding) {
                with(binding) {
                    viewModel = UserListItem(item, resourceProvider)
                    executePendingBindings()
                }
            }
        }
    }
}

private class UsersDiffCallback :
    DiffUtil.ItemCallback<GeneratedUserListItem>() {
    override fun areItemsTheSame(
        oldItem: GeneratedUserListItem,
        newItem: GeneratedUserListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GeneratedUserListItem,
        newItem: GeneratedUserListItem
    ): Boolean {
        return oldItem == newItem
    }
}
