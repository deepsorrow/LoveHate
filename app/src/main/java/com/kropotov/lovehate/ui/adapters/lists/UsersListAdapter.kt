package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.databinding.ListItemUserBinding
import com.kropotov.lovehate.data.items.UserListItem
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.fragment.UserListItem as GeneratedUserListItem
import javax.inject.Inject

class UsersListAdapter @Inject constructor(val resourceProvider: ResourceProvider)
    : PagingDataAdapter<GeneratedUserListItem, UsersListAdapter.ViewHolder>(UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            resourceProvider
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userListItem = getItem(position)
        if (userListItem != null) {
            holder.bind(userListItem)
        }
    }

    class ViewHolder(
        private val binding: ListItemUserBinding,
        private val resourceProvider: ResourceProvider
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GeneratedUserListItem) {
            with(binding) {
                viewModel = UserListItem(item, resourceProvider)
                executePendingBindings()
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
