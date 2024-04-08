package com.kropotov.lovehate.ui.adapters.lists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.utilities.stripThumbnail

class ImagesCarouselAdapter(
    private val items: MutableList<String>
) : RecyclerView.Adapter<ImagesCarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarouselViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_carousel_image, parent, false
            )
        )

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    @SuppressLint("NotifyDataSetChanged") // setItems invokes only once
    fun setItems(items: List<String>) {
        this.items.run {
            if (items.isEmpty()) {
                add("") // placeholder
            } else {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    class CarouselViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val carouselImageView = view.findViewById<ImageView>(R.id.carousel_image)
        private val errorPlaceholder = ResourcesCompat.getDrawable(
            view.resources,
            R.drawable.no_image_big_placeholder,
            null
        )
        fun bind(thumbnailUrl: String) {
            val fullImageUrl = thumbnailUrl.stripThumbnail()
            Glide.with(view)
                .load(fullImageUrl)
                .error(errorPlaceholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(Glide.with(view).load(thumbnailUrl))
                .into(carouselImageView)
        }
    }
}