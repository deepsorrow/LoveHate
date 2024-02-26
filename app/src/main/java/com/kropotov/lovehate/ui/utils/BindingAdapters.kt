package com.kropotov.lovehate.ui.utils

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.kropotov.lovehate.R

@BindingAdapter("textRes")
internal fun TextView.textRes(@StringRes textId: Int?) {
    text = when (textId) {
        null, 0 -> ""
        else    -> context.getString(textId)
    }
}

@BindingAdapter("onClick")
fun View.setActionOnClick(action: (() -> Unit)?) {
    setOnClickListener { action?.invoke() }
}

@BindingAdapter("colorByAttr")
internal fun setColorByAttr(view: TextView, @AttrRes attrResId: Int) {
    if (attrResId != 0) {
        val typedValue = TypedValue()
        val theme = view.context.theme
        theme.resolveAttribute(attrResId, typedValue, true)
        view.setTextColor(typedValue.data)
    }
}

@BindingAdapter("backgroundRes")
internal fun View.setBackgroundRes(@DrawableRes drawableRes: Int) {
    background = ResourcesCompat.getDrawable(resources, drawableRes, context.theme)
}

@BindingAdapter("isLoveTextColor")
internal fun TextView.setLoveHateTextColor(isLove: Boolean) {
    val attrColor = if (isLove) R.attr.love_color else R.attr.hate_color
    setColorByAttr(this, attrColor)
}

@BindingAdapter("isLove", "feelingPercent")
internal fun TextView.formatFeelingPercent(isLove: Boolean, percent: String) {
    val template = if (isLove) {
        context.getString(R.string.love_percent_feeling)
    } else {
        context.getString(R.string.hate_percent_feeling)
    }
    text = String.format(template, percent)
}