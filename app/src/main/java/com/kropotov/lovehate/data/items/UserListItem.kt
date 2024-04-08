package com.kropotov.lovehate.data.items

import com.kropotov.lovehate.R
import com.kropotov.lovehate.fragment.UserListItem as GeneratedUserListItem
import com.kropotov.lovehate.type.OpinionType
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.localize
import com.kropotov.lovehate.ui.utilities.plusServerIp
import java.util.Locale

data class UserListItem(
    private val user: GeneratedUserListItem,
    private val resourceProvider: ResourceProvider
) {
    val id get() = user.id
    val ratingPosition = "# ${user.position}"
    val photoUrl get() = user.photoUrl.plusServerIp()
    val username get() = user.username
    val topicsCount get() = user.topicsCount.toString()
    val opinionType get() = user.type
    val opinionsCount get() = user.opinionsCount.toString()
    val opinionPercent get() = user.opinionPercent.toString()

    val scoreLocalized = "${resourceProvider.getString(user.scoreTitle.localize())} (${user.score})"

    val isTopicsCountVisible = user.topicsCount > 0

    val isOpinionsCountVisible = user.opinionsCount > 0

    val heartIcon: Int = if (user.type == OpinionType.HATE) R.string.icon_broken_heart else R.string.icon_heart
}
