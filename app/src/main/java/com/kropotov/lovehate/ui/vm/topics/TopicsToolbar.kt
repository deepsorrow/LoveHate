package com.kropotov.lovehate.ui.vm.topics

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TopicsToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(androidx.appcompat.R.attr.colorPrimary)

    override val title = ObservableField(R.string.topics)
    override val subtitle = ObservableField(0)
    override val subtitleIsVisible = ObservableBoolean(false)

    override val searchIconIsVisible = ObservableBoolean(true)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val isBottomOffsetVisible = ObservableBoolean(false)

}