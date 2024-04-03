package com.kropotov.lovehate.ui.screens.opinions

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import javax.inject.Inject

class OpinionsHostToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(androidx.appcompat.R.attr.colorPrimary)

    override val title = ObservableField(R.string.app_name)
    override val subtitle = ObservableField(R.string.new_messages)
    override val isSubtitleVisible = ObservableBoolean(true)

    override val searchIconIsVisible = ObservableBoolean(true)
    override val searchText = ObservableField("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val isBottomOffsetVisible = ObservableBoolean(false)

}
