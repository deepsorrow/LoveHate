package com.kropotov.lovehate.ui.viewmodels.topicPage

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicsRepository
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageRouter
import com.kropotov.lovehate.ui.utilities.Favorite
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TopicPageViewModel @Inject constructor(
    @Named(TOPIC_PAGE_ID) val topicId: Int,
    val router: TopicPageRouter,
    private val repository: TopicsRepository
) : BaseViewModel(), Favorite {

    val title = ObservableField("")
    val isLoved = ObservableBoolean(false)
    val opinionsCount = ObservableField("")
    val feelingPercent = ObservableField("")
    val date = ObservableField("")
    val author = ObservableField("")

    val authorOpinionType  = ObservableField<OpinionType>()

    val heartIcon = ObservableInt(0)

    override var isFavorite: Boolean = false
    override var favoriteIcon = ObservableField(R.string.icon_favorite)
    override var favoriteIconColor = ObservableField(R.attr.unaccented_light_text_color)

    init {
        getTopicPage()
    }

    suspend fun getSimilarTopics() = repository.getSimilarTopics(topicId)

    private fun getTopicPage() {
        viewModelScope.launch {
            repository.getTopicPage(topicId)?.let {
                title.set(it.title)
                isLoved.set(it.opinionType == OpinionType.LOVE)
                opinionsCount.set(it.opinionsCount.toString())
                feelingPercent.set(it.percent.toString())
                date.set(it.date)
                author.set(it.author)
                isFavorite = it.isFavorite
                authorOpinionType.set(it.authorOpinionType)

                heartIcon.set(if (isLoved.get()) R.string.icon_heart_filled else R.string.icon_filled_broken_heart)
            }
        }
    }
}