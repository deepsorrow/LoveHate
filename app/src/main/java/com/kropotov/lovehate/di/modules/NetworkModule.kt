package com.kropotov.lovehate.di.modules

import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.BackendService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient = BackendService.create()
}