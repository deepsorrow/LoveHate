package com.kropotov.lovehate.di.app

import android.app.Application
import android.content.Context
import com.kropotov.lovehate.LoveHateApplication
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [AppModule.BindsDIModule::class],)
class AppModule {

    @Provides
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Module
    interface BindsDIModule {
        @Binds
        fun bindApplication(application: LoveHateApplication): Application
    }
}
