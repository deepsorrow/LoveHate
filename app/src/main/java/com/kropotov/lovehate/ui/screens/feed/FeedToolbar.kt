package com.kropotov.lovehate.ui.screens.feed

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FeedToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(androidx.appcompat.R.attr.colorPrimary)

    override val title = ObservableField(R.string.app_name)
    override val subtitle = ObservableField(R.string.new_messages)
    override val isSubtitleVisible = ObservableBoolean(true)

    override val searchIconIsVisible = ObservableBoolean(false)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val arrowBackAction = ObservableField { }
    override val isBottomOffsetVisible = ObservableBoolean(false)

}
