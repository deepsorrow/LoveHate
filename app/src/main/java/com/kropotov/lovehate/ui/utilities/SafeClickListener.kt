package com.kropotov.lovehate.ui.utilities

import android.os.SystemClock
import android.view.View

/**
 * Prevents clicking many times on the same button.
 */
class SafeClickListener(
    private val onSafeClick: (View?) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < DEFAULT_INTERVAL_MS) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }

    private companion object {
        private const val DEFAULT_INTERVAL_MS: Int = 1000
    }
}