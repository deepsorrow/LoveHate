package com.kropotov.lovehate.ui.adapters.lists

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.screens.opinions.OpinionsRouter
import com.kropotov.lovehate.ui.utilities.SafeClickListener
import com.kropotov.lovehate.ui.utilities.getActivity
import com.kropotov.lovehate.ui.utilities.stripThumbnail
import kotlinx.coroutines.launch

class OpinionAttachmentsListAdapter(
    private val router: OpinionsRouter,
    private val urls: List<String>
) : RecyclerView.Adapter<OpinionAttachmentsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_attachment, parent, false)

        return ViewHolder(view, router)
    }

    override fun getItemCount(): Int = urls.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = urls.getOrNull(position)
        if (url != null) {
            holder.bind(url)
        }
    }

    class ViewHolder(
        itemView: View,
        private val router: OpinionsRouter
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.thumbnail)

        fun bind(url: String) {
            Glide.with(imageView)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(onResourceReadyListener(router, imageView, url))
                .submit()
        }

        private fun onResourceReadyListener(
            router: OpinionsRouter,
            imageView: ImageView,
            url: String
        ) = object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, m: Any?, t: Target<Bitmap>, i: Boolean) =
                true

            override fun onResourceReady(b: Bitmap, m: Any, t: Target<Bitmap>?, d: DataSource, i: Boolean) =
                run {
                    imageView.context.getActivity()?.lifecycleScope?.launch {
                        imageView.setImageBitmap(b)
                        imageView.setImageClickListener(router, b, url)
                    }
                    true
                }
        }

        private fun ImageView.setImageClickListener(router: OpinionsRouter, thumbnailBitmap: Bitmap, url: String) {
            setOnClickListener(
                SafeClickListener {
                    router.navigateToAttachmentViewer(thumbnailBitmap, url.stripThumbnail())
                }
            )
        }
    }
}
