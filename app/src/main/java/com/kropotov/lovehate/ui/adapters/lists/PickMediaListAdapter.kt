package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.ui.utilities.SafeClickListener
import com.kropotov.lovehate.ui.utilities.getBitmap

class PickMediaListAdapter(
    private val onClickAction: (MediaListItem) -> Unit
) : RecyclerView.Adapter<PickMediaListAdapter.ViewHolder>() {

    var items = listOf<MediaListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_photo_gallery, parent, false)

        return ViewHolder(view, onClickAction)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaListItem = items.getOrNull(position)
        if (mediaListItem != null) {
            holder.bind(mediaListItem, position)
        }
    }

    class ViewHolder(
        itemView: View,
        private val onClickAction: (MediaListItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ShapeableImageView>(R.id.thumbnail)

        fun bind(item: MediaListItem, position: Int) {
            val thumbnail = itemView.context.getBitmap(item)

            imageView.apply {
                setImageBitmap(thumbnail)

                val cornerRadius = resources.getDimension(R.dimen.default_corner_radius)
                if (position == 0) {
                    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                        .build()
                } else if (position == 2) {
                    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                        .build()
                }
            }
            itemView.setOnClickListener(
                SafeClickListener {
                    onClickAction(item)
                }
            )
        }
    }
}