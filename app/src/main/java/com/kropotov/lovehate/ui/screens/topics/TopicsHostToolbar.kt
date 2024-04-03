package com.kropotov.lovehate.ui.screens.topics

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TopicsHostToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(androidx.appcompat.R.attr.colorPrimary)

    override val title = ObservableField(R.string.topics)
    override val subtitle = ObservableField(0)
    override val isSubtitleVisible = ObservableBoolean(false)

    override val searchIconIsVisible = ObservableBoolean(true)
    override val searchText = ObservableField("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val isBottomOffsetVisible = ObservableBoolean(false)

}
