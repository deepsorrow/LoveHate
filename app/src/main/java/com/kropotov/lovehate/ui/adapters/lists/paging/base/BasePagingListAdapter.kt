package com.kropotov.lovehate.ui.adapters.lists.paging.base

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagingListAdapter<Item : Any, ViewHolder : BasePagingViewHolder<Item>>(
    diffCallback: DiffUtil.ItemCallback<Item>
) : PagingDataAdapter<Item, ViewHolder>(diffCallback) {

    override fun getItemViewType(position: Int): Int =
        if (getItem(position) == null) {
            PLACEHOLDER_VIEW_TYPE
        } else {
            DEFAULT_VIEW_TYPE
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Placeholders binds as null.
        if (item != null) {
            holder.bind(item)
        }
    }

    protected companion object {
        const val PLACEHOLDER_VIEW_TYPE = 0
        const val DEFAULT_VIEW_TYPE = 1
    }
}