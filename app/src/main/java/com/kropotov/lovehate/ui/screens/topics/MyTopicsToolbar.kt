package com.kropotov.lovehate.ui.screens.topics

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MyTopicsToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(0)

    override val title = ObservableField(R.string.my_topics)
    override val subtitle = ObservableField(0)
    override val isSubtitleVisible = ObservableBoolean(false)

    override val searchIconIsVisible = ObservableBoolean(true)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(true)
    override val arrowBackAction = ObservableField { }
    override val isBottomOffsetVisible = ObservableBoolean(true)
}
