package com.kropotov.lovehate.ui.dialogs

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.repositories.FeedbackRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendFeedbackViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val repository: FeedbackRepository
) : BaseViewModel(resourceProvider) {

    val text = ObservableField("")
    private val _feedbackSentStream = MutableSharedFlow<Unit>()
    val feedbackSentStream: SharedFlow<Unit> = _feedbackSentStream

    fun onSendClicked() {
        viewModelScope.launch {
            if (text.get().orEmpty().length < MIN_LENGTH) {
                emitErrorMessage(R.string.send_feedback_error_min_length)
                return@launch
            }
            _isLoading.emit(true)
            withContext(Dispatchers.IO + defaultExceptionHandler) {
                repository.sendFeedback(text.get().orEmpty())
                _isLoading.emit(false)
                _feedbackSentStream.emit(Unit)
            }
        }
    }

    private companion object {
        const val MIN_LENGTH = 10
    }
}
