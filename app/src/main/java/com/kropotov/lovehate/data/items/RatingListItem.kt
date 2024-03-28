package com.kropotov.lovehate.data.items

abstract class RatingListItem(
    open var subtitle: String? = null,
    open val action: (() -> Unit)? = null
) {
    abstract val ratingType: Any
    abstract val title: Int
}
