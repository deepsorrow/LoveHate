package com.kropotov.lovehate.di.modules.fragments

import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment
import com.kropotov.lovehate.ui.fragments.topicPage.TopicPageFragment.Companion.TOPIC_PAGE_ID
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TopicPageFragmentModule {

    @Provides
    @Named(TOPIC_PAGE_ID)
    fun provideTopicId(fragment: TopicPageFragment) = fragment.arguments?.getInt(TOPIC_PAGE_ID) ?: 0
}