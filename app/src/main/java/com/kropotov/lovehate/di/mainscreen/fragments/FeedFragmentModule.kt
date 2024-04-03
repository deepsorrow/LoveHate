package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsHostFragment.Companion.FEED_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.utilities.getIntNullable
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FeedFragmentModule {

    @Provides
    @Named(FEED_TOPIC_ID_ARG)
    fun provideTopicId(fragment: OpinionsHostFragment) = fragment.arguments?.getIntNullable(FEED_TOPIC_ID_ARG)

}
