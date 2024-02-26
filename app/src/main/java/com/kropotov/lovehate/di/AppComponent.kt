package com.kropotov.lovehate.di

import com.kropotov.lovehate.LoveHateApplication
import com.kropotov.lovehate.di.modules.ActivityFragmentInjectorsModule
import com.kropotov.lovehate.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityFragmentInjectorsModule::class,
        AppModule::class
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