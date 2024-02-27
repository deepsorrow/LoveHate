package com.kropotov.lovehate.ui.utils

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

/** Returns Serializible in a new way for API 33+ and in the old way for previous API. */
inline fun <reified T : Serializable> Bundle.serializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(key) as T
    }
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T =
    this.apply {
        arguments = Bundle().apply(argsBuilder)
    }