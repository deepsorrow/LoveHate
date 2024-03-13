package com.kropotov.lovehate.ui.viewmodels.ratings

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.ToolbarContract
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class RatingsToolbar @Inject constructor() : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(androidx.appcompat.R.attr.colorPrimary)

    override val title = ObservableField(R.string.app_name)
    override val subtitle = ObservableField(R.string.new_messages)
    override val subtitleIsVisible = ObservableBoolean(true)

    override val searchIconIsVisible = ObservableBoolean(true)
    override val searchText = MutableStateFlow("")
    override val arrowBackIsVisible = ObservableBoolean(false)
    override val isBottomOffsetVisible = ObservableBoolean(false)

}