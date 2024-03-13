package com.kropotov.lovehate.di

import com.kropotov.lovehate.LoveHateApplication
import com.kropotov.lovehate.di.modules.AndroidInjectorsModule
import com.kropotov.lovehate.di.modules.AppModule
import com.kropotov.lovehate.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidInjectorsModule::class,
        AppModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    fun inject(application: LoveHateApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: LoveHateApplication): Builder

        fun build(): AppComponent
    }
}