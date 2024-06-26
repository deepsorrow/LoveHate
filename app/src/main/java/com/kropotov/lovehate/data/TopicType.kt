package com.kropotov.lovehate.data

import androidx.annotation.StringRes
import com.kropotov.lovehate.R

enum class TopicType(
    @StringRes val title: Int
) {
    RECENT(R.string.recent),
    NEW(R.string.new_topics),
    POPULAR(R.string.popular),
    MOST_LOVED(R.string.most_loved),
    MOST_HATED(R.string.most_hated),
    BY_CURRENT_USER(R.string.my_topics),
    FAVORITES(R.string.topics)
}
