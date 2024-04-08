package com.kropotov.lovehate.ui.adapters.lists.paging.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingViewHolder<Item>(
    binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: Item)
}