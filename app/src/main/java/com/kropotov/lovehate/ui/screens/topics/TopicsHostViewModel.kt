package com.kropotov.lovehate.ui.screens.topics

import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import javax.inject.Inject

class TopicsHostViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: TopicsToolbar
) : BaseViewModel(resourceProvider)
