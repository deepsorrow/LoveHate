package com.kropotov.lovehate.data

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.kropotov.lovehate.R

enum class OpinionType(
    @StringRes val title: Int,
    @AttrRes val color: Int,
    @AttrRes val containerColor: Int
) {
    UNION(R.string.icon_union, R.attr.union_color, R.attr.union_background_color),
    LOVE(R.string.love, R.attr.love_container_color, R.attr.love_background_color),
    INDIFFERENCE(R.string.neutral, R.attr.neutral_container_color, R.attr.neutral_background_color),
    HATE(R.string.hate, R.attr.hate_container_color, R.attr.hate_background_color)
}
