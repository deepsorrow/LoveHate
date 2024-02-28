package com.kropotov.lovehate.ui.vm.topic

import androidx.lifecycle.ViewModel
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.Topic
import javax.inject.Inject

class TopicPageVm @Inject constructor() : ViewModel() {

    val title: String = "кошки"
    val isLoved: Boolean = true
    val commentsCount: String = "14"
    val feelingPercent: String = "76"
    val date: String = "03/02/2023 Пн 16:22:08"
    val author: String = "Milita"
    val isFavorite: Boolean = false
    val authorOpinionType: OpinionType = OpinionType.LOVE
    val similarTopics: List<Topic> = listOf()

    val heartIcon: Int = if (isLoved) R.string.icon_heart_filled else R.string.icon_filled_broken_heart
}