package com.kropotov.lovehate.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class Analytics {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun logEvent() {
        firebaseAnalytics.logEvent(SHOW_REQUIRED_UPDATE_KEY) {
            TODO("Analytics feature")
        }
    }

    private companion object {
        const val SHOW_REQUIRED_UPDATE_KEY = "show_required_update"
    }
}