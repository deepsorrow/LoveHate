package com.kropotov.lovehate.data

import androidx.annotation.StringRes
import com.kropotov.lovehate.R

enum class UsersListType(
    @StringRes val title: Int
) {
    MOST_ACTIVE(R.string.most_active),
    MOST_TENDERHEARTED(R.string.most_tenderhearted),
    MOST_MANY_SIDED(R.string.most_many_sided),
    MOST_SPITEFUL(R.string.most_spiteful),
    MOST_OBSESSED(R.string.most_obsessed)
}
