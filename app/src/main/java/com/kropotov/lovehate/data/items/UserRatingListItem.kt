package com.kropotov.lovehate.data.items

import com.kropotov.lovehate.data.UsersListType

data class UserRatingListItem(
    override val ratingType: UsersListType,
    override var subtitle: String? = null,
    override val action: (() -> Unit)? = null
): RatingListItem(subtitle, action) {

    override val title = ratingType.title
}
