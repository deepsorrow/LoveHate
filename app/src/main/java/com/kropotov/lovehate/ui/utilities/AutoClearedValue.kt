package com.kropotov.lovehate.ui.utilities

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T : Any>(private val lifecycleOwner: LifecycleOwner) :
    ReadWriteProperty<LifecycleOwner, T>,
    LifecycleEventObserver {

    private var _value: T? by Delegates.vetoable(null) { _, old, new ->
        if (old != new && new != null) {
            clearBindingHandler.removeCallbacksAndMessages(TOKEN)
            lifecycleOwner.setLifecycleObserver(new)
        }
        true
    }
    private val clearBindingHandler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T =
        _value ?: throw IllegalStateException("Should never call auto-cleared-value get" +
                " when it might not be available")

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        _value = value
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            clearBindingHandler.postDelayed(0L, TOKEN) {
                _value = null
            }
        }
    }

    private fun LifecycleOwner.setLifecycleObserver(value: T) {
        val targetLifecycle = if (this is Fragment && value is ViewDataBinding) {
            viewLifecycleOwner.lifecycle
        } else {
            lifecycle
        }
        targetLifecycle.addObserver(this@AutoClearedValue)
    }

    private companion object {
        const val TOKEN = "auto_cleared_token"
    }
}

fun <T : Any> LifecycleOwner.autoCleared() = AutoClearedValue<T>(this)