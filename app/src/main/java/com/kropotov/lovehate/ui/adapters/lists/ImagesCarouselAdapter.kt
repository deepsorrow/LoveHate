package com.kropotov.lovehate.ui.adapters.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.utilities.stripThumbnail

class ImagesCarouselAdapter(
    var images: List<String>
) : RecyclerView.Adapter<ImagesCarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarouselViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_carousel_image, parent, false
            )
        )

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.count()

    class CarouselViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val carouselImageView = view.findViewById<ImageView>(R.id.carousel_image)

        fun bind(thumbnailUrl: String) {
            val fullImageUrl = thumbnailUrl.stripThumbnail()
            Glide.with(view)
                .load(fullImageUrl)
                .thumbnail(Glide.with(view).load(thumbnailUrl))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(carouselImageView)
        }
    }
}