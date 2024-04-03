package com.kropotov.lovehate.ui.utilities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Size
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloGraphQLException
import com.google.android.material.color.MaterialColors
import com.kropotov.lovehate.BuildConfig
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.type.UserScoreTitle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.io.Serializable
import java.security.AccessController.getContext


inline fun <reified T : Serializable> Bundle.serializable(key: String, clazz: Class<T>): T? {
    return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(key) as T
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T =
    this.apply {
        arguments = Bundle().apply(argsBuilder)
    }

fun Context.getColorAttr(@AttrRes attr: Int, @ColorInt defaultColor: Int = Color.WHITE) =
    MaterialColors.getColor(this, attr, defaultColor)

fun Resources.getDrawableRes(@DrawableRes drawableRes: Int) =
    ResourcesCompat.getDrawable(this, drawableRes, null)

fun Bundle.getIntNullable(key: String): Int? = getInt(key).run {
    if (this == 0) null else this
}

fun Window.adjustSystemBarIconsColor() {
    val backgroundColor = context.getColorAttr(R.attr.background_color)
    if (isColorDark(backgroundColor)) {
        WindowCompat.getInsetsController(this, decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    } else {
        WindowCompat.getInsetsController(this, decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }
}

fun isColorDark(color: Int): Boolean {
    val darkness =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
    return if (darkness < 0.5) {
        false // It's a light color
    } else {
        true // It's a dark color
    }
}

@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> = callbackFlow {
    val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
    awaitClose { removeTextChangedListener(listener) }
}.onStart { emit(text) }

fun Throwable.extractErrorMessage() = if (this is ApolloGraphQLException) {
    error.message
} else if (this is ApolloException) {
    cause?.message.orEmpty()
} else {
    message
}.orEmpty()

fun String.plusServerIp() = BuildConfig.SERVER_IP + "/" + this

fun String.stripThumbnail() = replace("thumbnails/", "")

@StringRes
fun UserScoreTitle.localize() = when(this) {
    UserScoreTitle.SURVEYOR -> R.string.surveyour
    UserScoreTitle.INSTIGATOR -> R.string.instigator
    UserScoreTitle.ENLIGHTENED -> R.string.enlightened
    UserScoreTitle.JUDGE -> R.string.judge
    UserScoreTitle.FORSETI -> R.string.forseti
    else -> 0
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

tailrec fun Context.getActivity(): AppCompatActivity? = this as? AppCompatActivity
    ?: (this as? ContextWrapper)?.baseContext?.getActivity()

fun Context.getBitmap(item: MediaListItem): Bitmap {
    val thumbnailSize = resources.getInteger(R.integer.thumbnail_size)
    return if (SDK_INT >= Build.VERSION_CODES.Q) {
        val cancellationSignal = CancellationSignal()
        contentResolver.loadThumbnail(
            item.uri!!,
            Size(thumbnailSize, thumbnailSize),
            cancellationSignal
        )
    } else {
        MediaStore.Images.Thumbnails.getThumbnail(
            contentResolver,
            item.id,
            MediaStore.Images.Thumbnails.MINI_KIND,
            null
        )
    }
}

