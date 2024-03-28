package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.data.items.RatingListItem
import com.kropotov.lovehate.databinding.ListItemRatingBinding

class RatingsListAdapter(
    private var items: List<RatingListItem>
) : RecyclerView.Adapter<RatingsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun updateItem(item: RatingListItem) {
        val position = items.indexOf(items.first { it.ratingType == item.ratingType })
        items[position].subtitle = item.subtitle
        notifyItemChanged(position)
    }

    class ViewHolder(
        private val binding: ListItemRatingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RatingListItem) {
            binding.viewModel = item
            binding.subtitleShimmer.run {
                visibility = if (item.subtitle == null) {
                    startShimmer()
                    View.VISIBLE
                } else {
                    hideShimmer()
                    View.GONE
                }
            }
        }
    }
}

