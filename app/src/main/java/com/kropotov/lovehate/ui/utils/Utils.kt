package com.kropotov.lovehate.ui.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/** Returns Serializible in a new way for API 33+ and in the old way for previous API. */
inline fun <reified T : Serializable> Bundle.serializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        getSerializable(key) as T
    }
}

/** Returns Parcelable in a new way for API 33+ and in the old way for previous API. */
fun <T : Parcelable> Bundle.parcelable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key)
    }
}