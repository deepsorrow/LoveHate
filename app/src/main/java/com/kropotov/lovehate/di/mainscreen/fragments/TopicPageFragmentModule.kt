package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TopicPageFragmentModule {

    @Provides
    @Named(TOPIC_PAGE_ID)
    fun provideForPageTopicId(fragment: TopicPageFragment) = fragment.arguments?.getInt(TOPIC_PAGE_ID) ?: 0
}
