package com.kropotov.lovehate.ui.screens.favorites

import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: FavoritesToolbar
) : BaseViewModel(resourceProvider)
