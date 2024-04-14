package com.kropotov.lovehate.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Reusable
import javax.inject.Inject

@Reusable
class Analytics @Inject constructor(
    applicationContext: Context
) {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)

    fun send(event: AnalyticsEvent) =
        firebaseAnalytics.logEvent(event.key, event.extras)
}