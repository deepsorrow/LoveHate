package com.kropotov.lovehate.ui.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class ToolbarVm : ViewModel() {
    val title = MutableStateFlow("")
    val subtitle = MutableStateFlow("")
    val subtitleIsVisible = MutableStateFlow(true)

    val searchIconIsVisible = MutableStateFlow(false)
    val searchText = MutableSharedFlow<String>()
    val arrowBackIsVisible = MutableStateFlow(false)
    val isBottomOffsetVisible = MutableStateFlow(true)
}