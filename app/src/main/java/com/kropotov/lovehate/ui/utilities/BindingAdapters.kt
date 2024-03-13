package com.kropotov.lovehate.ui.utilities

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.type.OpinionType

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

@BindingAdapter("backgroundColorByAttr")
internal fun setBackgroundColorByAttr(view: View, @AttrRes attrResId: Int) {
    if (attrResId != 0) {
        val typedValue = TypedValue()
        val theme = view.context.theme
        theme.resolveAttribute(attrResId, typedValue, true)
        view.setBackgroundColor(typedValue.data)
    }
}

@BindingAdapter("backgroundRes")
internal fun View.setBackgroundRes(@DrawableRes drawableRes: Int) {
    background = ResourcesCompat.getDrawable(resources, drawableRes, context.theme)
}

@BindingAdapter("isVisible")
internal fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
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

@BindingAdapter("messagesCount")
internal fun TextView.formatMessagesCount(messagesCount: String) {
    val template = if (messagesCount == "1") {
        context.getString(R.string.messages_count_template_single)
    } else {
        context.getString(R.string.messages_count_template_multiple)
    }
    text = String.format(template, messagesCount)
}

@BindingAdapter("authorOpinion")
internal fun TextView.formatAuthorOpinion(opinionSortType: OpinionType?) {
    val stringRes = when (opinionSortType) {
        OpinionType.LOVE -> R.string.posted_with_love
        OpinionType.HATE -> R.string.posted_with_hate
        else -> R.string.posted_with_neutral
    }
    text = context.getString(stringRes)
}