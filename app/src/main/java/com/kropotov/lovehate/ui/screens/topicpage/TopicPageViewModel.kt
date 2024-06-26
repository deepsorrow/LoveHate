package com.kropotov.lovehate.ui.screens.topicpage

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.analytics.Analytics
import com.kropotov.lovehate.analytics.AnalyticsEvent
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.data.repositories.TopicsRepository
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.Favorite
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.data.items.TopicListItem
import com.kropotov.lovehate.ui.utilities.plusServerIp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TopicPageViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    @Named(TOPIC_PAGE_ID) val topicId: Int,
    private val repository: TopicsRepository,
    private val analytics: Analytics
) : BaseViewModel(resourceProvider), Favorite {

    val title = ObservableField("")
    val opinionType = ObservableField<OpinionType>()
    val opinionsCount = ObservableField("")
    val feelingPercent = ObservableField("")
    val date = ObservableField("")
    val author = ObservableField("")
    val authorOpinionType  = ObservableField<OpinionType>()
    val heartIcon = ObservableInt(R.string.icon_heart_filled)

    val isTopicShimmerVisible = ObservableField(true)
    val isSimilarTopicsShimmerVisible = ObservableField(true)

    override var isFavorite = false
        set(value) {
            field = value
            updateFavoriteIcon()
        }
    override var isFavoriteFetching = false
    override var favoriteIcon = ObservableField(R.string.icon_favorite)
    override var favoriteIconColor = ObservableField(R.attr.unaccented_light_text_color)

    private val _similarTopics: MutableStateFlow<List<TopicListItem>> = MutableStateFlow(listOf())
    val similarTopics: StateFlow<List<TopicListItem>> = _similarTopics

    private val _carouselImages: MutableSharedFlow<List<String>> = MutableStateFlow(listOf())
    val carouselImages: SharedFlow<List<String>> = _carouselImages
    val hasManyImages = ObservableBoolean(false)

    init {
        loadTopicPage()
        loadSimilarTopics()
    }

    private fun loadTopicPage() {
        viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
            repository.getTopicPage(topicId).let {
                title.set(it.title)
                opinionType.set(it.opinionType)
                opinionsCount.set(it.opinionsCount.toString())
                feelingPercent.set(it.percent.toString())
                isTopicShimmerVisible.set(false)
                date.set(it.createdAt)
                author.set(it.author)
                isFavorite = it.isFavorite
                authorOpinionType.set(it.authorOpinionType)

                if (opinionType.get() == OpinionType.HATE)  {
                    heartIcon.set(R.string.icon_filled_broken_heart)
                }

                _carouselImages.emit(it.attachmentsUrls.map { url -> url.plusServerIp() })
                hasManyImages.set(it.attachmentsUrls.count() >= 2)

                analytics.send(AnalyticsEvent.ShowTopicPage(it.title))
            }
        }
    }

    private fun loadSimilarTopics() {
        viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
            val topics = repository.getSimilarTopics(topicId)
            isSimilarTopicsShimmerVisible.set(false)
            _similarTopics.emit(topics)
        }
    }

    override fun onFavoriteClick() {
        super.onFavoriteClick()

        isFavoriteFetching = true
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                emitMessage(R.string.unknown_error, InformType.ERROR, exception.message.orEmpty())
                isFavorite = !isFavorite
            }
        }
        viewModelScope.launch(Dispatchers.IO + handler) {
            isFavorite = repository.updateFavorite(topicId)
        }
    }
}
