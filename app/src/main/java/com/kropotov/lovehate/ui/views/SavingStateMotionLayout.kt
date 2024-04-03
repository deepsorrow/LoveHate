package com.kropotov.lovehate.ui.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.parcelize.Parcelize

/**
 * Saves [MotionLayout] state on configuration change.
 */
class SavingStateMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable {
        return SaveState(super.onSaveInstanceState(), startState, endState, targetPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SaveState)?.let {
            super.onRestoreInstanceState(it.superParcel)
            setTransition(it.startState, it.endState)
            progress = it.progress
        }
    }

    @Parcelize
    private class SaveState(
        val superParcel: Parcelable?,
        val startState: Int,
        val endState: Int,
        val progress: Float
    ) : Parcelable
}