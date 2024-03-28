package com.kropotov.lovehate.data.items

import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.TopicListItem as TopicListItemGenerated
import com.kropotov.lovehate.type.OpinionType

class TopicListItem(
    private val topicListItem: TopicListItemGenerated
) {
    val id: Int get() = topicListItem.id
    val title: String get() = topicListItem.title
    val opinionType: OpinionType get() = topicListItem.opinionType
    val opinionsCount: String get() = topicListItem.opinionsCount.toString()
    val percent: String get() = topicListItem.opinionPercent.toString()
    val heartIcon: Int
        get() = if (opinionType == OpinionType.HATE) R.string.icon_broken_heart else R.string.icon_heart
}
