package com.kropotov.lovehate.data.items

class MediaListItem(
    val path: String? = null
) {

    val isEmpty
        get() = path.orEmpty().isEmpty()
}
