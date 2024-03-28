package com.kropotov.lovehate.ui.screens.contribute

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ContributeToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(0)

    override val title = ObservableField(R.string.new_topic)
    override val subtitle = ObservableField(0)
    override val isSubtitleVisible = ObservableBoolean(false)

    override val searchIconIsVisible = ObservableBoolean(false)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val arrowBackAction = ObservableField { }
    override val isBottomOffsetVisible = ObservableBoolean(true)

}
