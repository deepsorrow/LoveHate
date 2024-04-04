package com.kropotov.lovehate.ui.utilities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kropotov.lovehate.R
import com.google.android.material.R as RMaterial
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.views.SavingStateMotionLayout


@BindingAdapter("textRes")
internal fun TextView.textRes(@StringRes textId: Int?) {
    text = when (textId) {
        null, 0 -> ""
        else -> context.getString(textId)
    }
}

@BindingAdapter("onClick")
fun View.setActionOnClick(action: (() -> Unit)) {
    setOnClickListener(SafeClickListener { action.invoke() })
}

// Useful for motion layout, because onClickListener overrides motion layout callback.
@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("onTouchShowKeyboard")
fun View.onTouchShowKeyboard(predicate: Boolean) {
    !predicate && return

    setOnClickListener {
        (parent as SavingStateMotionLayout).transitionToEnd()

        val delay =
            context.resources.getInteger(RMaterial.integer.material_motion_duration_medium_2).toLong()
        Handler(Looper.myLooper()!!).postDelayed({
            (parent as ViewGroup).findViewById<EditText>(R.id.search_edit_text)?.let { editText ->
                editText.requestFocus()
                editText.showKeyboard()
            }
        }, delay)
    }
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("onTouchResetSearchQuery")
fun View.onTouchResetSearchQuery(predicate: Boolean) {
    !predicate && return

    setOnClickListener {
        (parent as SavingStateMotionLayout).transitionToStart()

        val delay =
            context.resources.getInteger(RMaterial.integer.material_motion_duration_medium_2).toLong()
        Handler(Looper.myLooper()!!).postDelayed({
            (parent as ViewGroup).findViewById<EditText>(R.id.search_edit_text)?.let { editText ->
                editText.hideKeyboard()
                editText.setText("")
            }
        }, delay)
    }
}

@BindingAdapter("onClickPassView")
fun View.setActionOnClickPassView(action: ((v: View?) -> Unit)) {
    setOnClickListener(SafeClickListener(action))
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

@BindingAdapter("isNotGone")
internal fun View.isNotGone(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

@BindingAdapter("isVisible")
internal fun View.isVisible(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("isLoveTextColor")
internal fun TextView.setLoveHateTextColor(opinionType: OpinionType?) {
    opinionType == null && return

    val attrColor = when (opinionType) {
        OpinionType.LOVE -> R.attr.love_color
        OpinionType.HATE -> R.attr.hate_color
        else -> R.attr.unaccented_text_color
    }
    setColorByAttr(this, attrColor)
}

@BindingAdapter("isLove", "opinionPercent")
internal fun TextView.formatOpinionPercent(opinionType: OpinionType?, percent: String) {
    opinionType == null && return

    val template = when (opinionType) {
        OpinionType.LOVE -> context.getString(R.string.love_percent_feeling)
        OpinionType.HATE -> context.getString(R.string.hate_percent_feeling)
        else -> context.getString(R.string.debating)
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

@BindingAdapter("imageUrl", "thumbnailBitmap", requireAll = false)
fun ImageView.setImageUrl(url: String?, thumbnailBitmap: Bitmap?) {
    val placeholder = thumbnailBitmap?.toDrawable(resources) ?: ResourcesCompat.getDrawable(
        resources,
        R.drawable.no_image_placeholder,
        null
    )
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
