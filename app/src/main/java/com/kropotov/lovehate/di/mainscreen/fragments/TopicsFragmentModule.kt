package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import com.kropotov.lovehate.ui.utilities.serializable
import dagger.Module
import dagger.Provides

@Module
class TopicsFragmentModule {

    @Provides
    fun provideSortType(fragment: TopicsFragment) = fragment.arguments?.serializable(
        TopicsFragment.TOPIC_CATEGORY,
        TopicType::class.java
    ) ?: TopicType.RECENT
}
