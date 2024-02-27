package com.kropotov.lovehate.ui.vm.topics

import androidx.lifecycle.ViewModel
import com.kropotov.lovehate.R

class TopicListItemVm(
    val title: String,
    val isLoved: Boolean,
    val commentsCount: String,
    val feelingPercent: String
) : ViewModel() {

    val heartIcon: Int = if (isLoved) R.string.icon_heart else R.string.icon_broken_heart
}