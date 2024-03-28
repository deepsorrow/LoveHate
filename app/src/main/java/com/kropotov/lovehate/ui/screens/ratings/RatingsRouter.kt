package com.kropotov.lovehate.ui.screens.ratings

import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.ratings.fragments.RatingsFragment
import com.kropotov.lovehate.ui.screens.users.UsersFragment
import javax.inject.Inject

class RatingsRouter @Inject constructor(
    fragment: RatingsFragment
) : BaseRouter(fragment.childFragmentManager) {

    fun navigateToOpinions(listType: OpinionsListType)
        = navigateWithSlideUpTransition(OpinionsFragment.newInstance(listType))

    fun navigateToUsers(listType: UsersListType)
        = navigateWithSlideUpTransition(UsersFragment.newInstance(listType))
}
