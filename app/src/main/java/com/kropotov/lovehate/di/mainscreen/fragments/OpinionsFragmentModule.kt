package com.kropotov.lovehate.di.mainscreen.fragments

import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_FILTER_TYPE_ARG
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_TOPIC_ID_ARG
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment.Companion.OPINIONS_SORT_TYPE_ARG
import com.kropotov.lovehate.ui.utilities.getIntNullable
import com.kropotov.lovehate.ui.utilities.serializable
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OpinionsFragmentModule {

    @Provides
    @Named(OPINIONS_SORT_TYPE_ARG)
    fun provideSortType(fragment: OpinionsFragment) = fragment.arguments?.serializable(
        OPINIONS_SORT_TYPE_ARG,
        OpinionType::class.java
    ) ?: OpinionType.UNION

    @Provides
    @Named(OPINIONS_FILTER_TYPE_ARG)
    fun provideFilterType(fragment: OpinionsFragment) = fragment.arguments?.serializable(
        OPINIONS_FILTER_TYPE_ARG,
        OpinionsListType::class.java
    ) ?: OpinionsListType.ALL

    @Provides
    @Named(OPINIONS_TOPIC_ID_ARG)
    fun provideTopicId(fragment: OpinionsFragment) = fragment.arguments?.getIntNullable(
        OPINIONS_TOPIC_ID_ARG
    )
}
