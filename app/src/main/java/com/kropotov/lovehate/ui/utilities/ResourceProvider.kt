package com.kropotov.lovehate.ui.utilities

import android.content.Context
import androidx.annotation.StringRes
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ResourceProvider @Inject constructor(
    private val applicationContext: Context
) {

    fun getString(@StringRes id: Int, vararg args: String) =
        String.format(applicationContext.getString(id), *args)
}
