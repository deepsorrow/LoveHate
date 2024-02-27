package com.kropotov.lovehate.ui.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToolbarVm @Inject constructor(
    applicationContext: Context
) : ViewModel() {
    val title = MutableStateFlow("")
    val subtitle = MutableStateFlow("")
    val subtitleIsVisible = MutableStateFlow(true)

    val searchIconIsVisible = MutableStateFlow(false)
    val searchText = MutableSharedFlow<String>()
    val arrowBackIsVisible = MutableStateFlow(false)
    val isBottomOffsetVisible = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            title.emit(applicationContext.getString(R.string.app_name))
            subtitle.emit(applicationContext.getString(R.string.new_messages))
        }
    }
}