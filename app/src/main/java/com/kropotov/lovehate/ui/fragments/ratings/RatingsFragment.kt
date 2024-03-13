package com.kropotov.lovehate.ui.fragments.ratings

import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentRatingsBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.viewmodels.ratings.RatingsViewModel

class RatingsFragment : BaseFragment<RatingsViewModel, FragmentRatingsBinding>(
    R.layout.fragment_ratings
) {

    override val vmClass = RatingsViewModel::class.java

}