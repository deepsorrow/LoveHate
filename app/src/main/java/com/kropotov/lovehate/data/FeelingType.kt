package com.kropotov.lovehate.data

import android.os.Parcelable
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.kropotov.lovehate.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FeelingType(
    @StringRes val title: Int,
    @AttrRes val color: Int,
    @AttrRes val containerColor: Int
) : Parcelable {
    UNION(R.string.icon_union, R.attr.union_color, R.attr.union_background_color),
    LOVE(R.string.love, R.attr.love_color, R.attr.love_background_color),
    NEUTRAL(R.string.neutral, R.attr.neutral_color, R.attr.neutral_background_color),
    HATE(R.string.hate, R.attr.hate_color, R.attr.hate_background_color)
}