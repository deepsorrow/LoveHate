package com.kropotov.lovehate.ui.utilities

import androidx.databinding.ObservableField
import com.kropotov.lovehate.R

interface Favorite {

    var isFavorite: Boolean
    var favoriteIcon: ObservableField<Int>
    var favoriteIconColor: ObservableField<Int>

    fun onFavoriteClick() {
        isFavorite = !isFavorite
        updateFavoriteIcon()
    }

    fun updateFavoriteIcon() {
        if (isFavorite) {
            favoriteIcon.set(R.string.icon_favorite_filled)
            favoriteIconColor.set(R.attr.favorite_icon_color)
        } else {
            favoriteIcon.set(R.string.icon_favorite)
            favoriteIconColor.set(R.attr.unaccented_text_color)
        }
    }
}