package com.kropotov.lovehate.ui.screens.contribute

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.repositories.TopicsRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.views.LoveHateTextField.OpinionStateListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContributeViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val toolbar: ContributeToolbar,
    private val repository: TopicsRepository
) : BaseViewModel(resourceProvider), OpinionStateListener {

    var title = ObservableField("")
    var opinion = ObservableField("")

    private val _opinionState = MutableStateFlow(OpinionType.LOVE)
    val opinionType: StateFlow<OpinionType> = _opinionState

    private val _navigateToNewTopic = MutableSharedFlow<Int>()
    val navigateToNewTopic: SharedFlow<Int> = _navigateToNewTopic

    override fun onLoveClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.LOVE)
        }
    }

    override fun onHateClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.HATE)
        }
    }

    override fun onNeutralClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.INDIFFERENCE)
        }
    }

    fun onPublishClicked() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                _isLoading.emit(false)
                emitErrorMessage(R.string.unknown_error, exception.message.orEmpty())
            }
        }
        viewModelScope.launch(handler) {
            _isLoading.emit(true)

            withContext(Dispatchers.IO) {
                repository.createTopic(
                    title = title.get().orEmpty(),
                    opinionType = opinionType.value,
                    comment = opinion.get().orEmpty()
                ).let {
                    _navigateToNewTopic.emit(it)

                    _isLoading.emit(false)
                }
            }
        }
    }
}
