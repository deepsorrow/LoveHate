package com.kropotov.lovehate.data

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.kropotov.lovehate.R

enum class AppTheme(
    @StringRes val titleResId: Int,
    @StyleRes val themeResId: Int
) {
    LIGHT(R.string.light_theme, R.style.Theme_LoveHate_Light),
    DARK(R.string.dark_theme, R.style.Theme_LoveHate_Dark)
}
