package com.kropotov.lovehate.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.kropotov.lovehate.ui.screens.topics.TopicsViewModel
import com.kropotov.lovehate.ui.screens.opinions.OpinionsViewModel
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.profile.ProfileRouter

sealed class AnalyticsEvent(val key: String, val extras: Bundle? = null) {

    /**
     * On [TopicsViewModel.searchTopics] with non empty query.
     */
    class SearchTopics(query: String) :
        AnalyticsEvent(SEARCH_TOPICS_KEY, bundleOf(QUERY_PARAM to query))

    /**
     * On [OpinionsViewModel.searchOpinions] with non empty query.
     */
    class SearchOpinions(query: String) :
        AnalyticsEvent(SEARCH_OPINIONS_KEY, bundleOf(QUERY_PARAM to query))

    /**
     * On show [TopicPageFragment].
     */
    class ShowTopicPage(title: String) :
        AnalyticsEvent(SHOW_TOPIC_PAGE, bundleOf(TITLE_PARAM to title))

    /**
     * On show [TopicsFragment].
     */
    class ShowTopicsList(listType: String) :
        AnalyticsEvent(SHOW_TOPICS_LIST, bundleOf(LIST_TYPE_PARAM to listType))

    /**
     * On show [OpinionsFragment].
     */
    class ShowOpinionsList(listType: String, topicId: Int?) :
        AnalyticsEvent(
            SHOW_OPINIONS_LIST,
            bundleOf(LIST_TYPE_PARAM to listType, ID_PARAM to topicId)
        )

    /**
     * On theme change through [ProfileRouter.onAppThemeClicked].
     */
    class ChangeAppTheme(theme: String) :
        AnalyticsEvent(CHANGE_APP_THEME, bundleOf(THEME_PARAM to theme))

    override fun equals(other: Any?): Boolean =
        if (other is AnalyticsEvent) {
            key == other.key
        } else {
            false
        }

    override fun hashCode(): Int = key.hashCode()

    private companion object {
        const val SEARCH_TOPICS_KEY = "search_topics"
        const val SEARCH_OPINIONS_KEY = "search_opinions"
        const val SHOW_TOPIC_PAGE = "show_topic_page"
        const val SHOW_TOPICS_LIST = "show_topics"
        const val SHOW_OPINIONS_LIST = "show_opinions"
        const val CHANGE_APP_THEME = "change_app_theme"

        const val QUERY_PARAM = "query"
        const val TITLE_PARAM = "title"
        const val ID_PARAM = "id"
        const val LIST_TYPE_PARAM = "list_type"
        const val THEME_PARAM = "theme"
    }
}