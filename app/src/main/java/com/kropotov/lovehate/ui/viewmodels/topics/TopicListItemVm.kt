package com.kropotov.lovehate.ui.viewmodels.topics

import androidx.lifecycle.ViewModel
import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.TopicListItem
import com.kropotov.lovehate.type.OpinionType

class TopicListItemVm(
    private val topicListItem: TopicListItem
) : ViewModel() {

    val id: Int
        get() = topicListItem.id

    val title: String
        get() = topicListItem.title

    val isLoved: Boolean
        get() = topicListItem.opinionType == OpinionType.LOVE

    val opinionsCount: String
        get() = topicListItem.opinionsCount.toString()

    val percent: String
        get() = topicListItem.percent.toString()

    val heartIcon: Int = if (isLoved) R.string.icon_heart else R.string.icon_broken_heart
}