package com.kropotov.lovehate.ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
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

fun Context.getColorAttr(@AttrRes attr: Int, @ColorInt defaultColor: Int = Color.WHITE)
    = MaterialColors.getColor(this, attr, defaultColor)

fun Resources.getDrawableRes(@DrawableRes drawableRes: Int) =
    ResourcesCompat.getDrawable(this, drawableRes, null)