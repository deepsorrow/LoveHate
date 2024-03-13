package com.kropotov.lovehate.di.modules.fragments

import com.kropotov.lovehate.data.TopicsSortType
import com.kropotov.lovehate.ui.fragments.topics.TopicsFragment
import com.kropotov.lovehate.ui.utilities.serializable
import dagger.Module
import dagger.Provides

@Module
class TopicsFragmentModule {

    @Provides
    fun provideSortType(fragment: TopicsFragment) = fragment.arguments?.serializable(
        TopicsFragment.TOPIC_CATEGORY,
        TopicsSortType::class.java
    ) ?: TopicsSortType.RECENT
}