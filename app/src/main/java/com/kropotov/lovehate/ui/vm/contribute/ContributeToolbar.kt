package com.kropotov.lovehate.ui.vm.contribute

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
    override val subtitleIsVisible = ObservableBoolean(true)

    override val searchIconIsVisible = ObservableBoolean(false)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val isBottomOffsetVisible = ObservableBoolean(false)

}