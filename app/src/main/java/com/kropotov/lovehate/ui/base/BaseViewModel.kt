package com.kropotov.lovehate.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.extractErrorMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _informMessageStream = MutableSharedFlow<InformMessage>()
    val informMessageStream: SharedFlow<InformMessage> = _informMessageStream

    val defaultExceptionHandler get() = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            emitErrorMessage(R.string.unknown_error, exception.extractErrorMessage())
        }
    }

    suspend fun emitErrorMessage(@StringRes stringRes: Int, vararg args: String) {
        val text = resourceProvider.getString(stringRes, *args)
        _informMessageStream.emit(InformMessage(InformType.ERROR, text))
    }
}
