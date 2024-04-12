package com.kropotov.lovehate

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.kropotov.lovehate.di.app.DaggerAppComponent
import com.kropotov.lovehate.di.app.AppComponent
import com.kropotov.lovehate.ui.utilities.createNotificationChannel
import com.kropotov.lovehate.workers.NotificationWorker
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class LoveHateApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            initStrictMode()
        }
        super.onCreate()

        createNotificationChannels()
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectCustomSlowCalls()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val title = getString(R.string.notification_channel_title_reactions)
            createNotificationChannel(
                NotificationWorker.NOTIFICATION_CHANNEL_ID,
                title
            )
        }
    }

    override fun androidInjector() = dispatchingAndroidInjector
}
