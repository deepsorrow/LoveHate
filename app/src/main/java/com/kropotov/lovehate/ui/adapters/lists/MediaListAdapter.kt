package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem

class MediaListAdapter(
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<MediaListAdapter.ViewHolder>() {

    private var items = mutableListOf(MediaListItem())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == EMPTY_MEDIA_TYPE) {
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_media, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_photo_thumbnail, parent, false)
        }

        return ViewHolder(view, fragmentManager)
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int =
        if (items[position].isEmpty) EMPTY_MEDIA_TYPE else MEDIA_TYPE


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaListItem = items.getOrNull(position)
        if (mediaListItem != null) {
            holder.bind(mediaListItem)
        }
    }

    fun addMedia(item: MediaListItem) {
        items.add(item)
    }

    class ViewHolder(
        itemView: View,
        private val fragmentManager: FragmentManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MediaListItem) {
            if (item.isEmpty) {
                itemView.findViewById<TextView>(R.id.add_media).setOnClickListener {

//                    val pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest> =
//                        registerForActivityResult(PickMultipleVisualMedia(5)) { uris ->
//                            if (!uris.isEmpty()) {
//                                Log.d(
//                                    "PhotoPicker",
//                                    "Number of items selected: " + uris.size()
//                                )
//                            } else {
//                                Log.d("PhotoPicker", "No media selected")
//                            }
//                        }
//
//                    pickMultipleMedia.launch(
//                        Builder()
//                            .setMediaType(ImageAndVideo)
//                            .build()
//                    )

                    //PickMediaDialog().show(fragmentManager, null)
                }
            } else {
                // Open media
            }
        }
    }

    companion object {
        const val EMPTY_MEDIA_TYPE = 0
        const val MEDIA_TYPE = 1
    }
}
