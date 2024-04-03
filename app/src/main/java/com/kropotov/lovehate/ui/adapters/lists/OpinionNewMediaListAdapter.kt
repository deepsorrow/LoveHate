package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.ui.dialogs.pickmedia.PickMediaDialog
import com.kropotov.lovehate.ui.dialogs.newopinion.NewOpinionViewModel.Companion.MAX_MEDIA_COUNT
import com.kropotov.lovehate.ui.utilities.SafeClickListener
import com.kropotov.lovehate.ui.utilities.getBitmap

class OpinionNewMediaListAdapter(
    private val mediaPaths: MutableList<MediaListItem>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<OpinionNewMediaListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == EMPTY_MEDIA_TYPE) {
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_media, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_selected_photo, parent, false)
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mediaPaths.count()

    override fun getItemViewType(position: Int): Int =
        if (mediaPaths[position].isEmpty) EMPTY_MEDIA_TYPE else MEDIA_TYPE


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaListItem = mediaPaths.getOrNull(position)
        if (mediaListItem != null) {
            holder.bind(mediaListItem)
        }
    }

    fun addMedia(item: MediaListItem) {
        if (mediaPaths.last().isEmpty) {
            mediaPaths.removeLast()
            notifyItemRemoved(mediaPaths.count() - 1)
        }
        mediaPaths.add(item)
        notifyItemInserted(mediaPaths.count() - 1)

        addNewMediaButtonIfNeeded()
    }

    private fun removeMedia(item: MediaListItem) {
        val position = mediaPaths.indexOf(item)
        mediaPaths.remove(item)
        notifyItemRemoved(position)

        addNewMediaButtonIfNeeded()
    }

    private fun addNewMediaButtonIfNeeded() {
        if (mediaPaths.count() < MAX_MEDIA_COUNT && !mediaPaths.last().isEmpty) {
            mediaPaths.add(MediaListItem())
            notifyItemInserted(mediaPaths.count() - 1)
        }
    }

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MediaListItem) {
            if (item.isEmpty) {
                itemView.findViewById<TextView>(R.id.add_media).setOnClickListener(
                    SafeClickListener { PickMediaDialog().show(fragmentManager, null) }
                )
            } else {
                val bitmap = itemView.context.getBitmap(item)
                itemView.findViewById<ImageView>(R.id.thumbnail).setImageBitmap(bitmap)
                itemView.findViewById<TextView>(R.id.close_btn).setOnClickListener(
                    SafeClickListener { removeMedia(item) }
                )
            }
        }
    }

    companion object {
        const val EMPTY_MEDIA_TYPE = 0
        const val MEDIA_TYPE = 1
    }
}
