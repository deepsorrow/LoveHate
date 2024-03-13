package com.kropotov.lovehate.ui.viewmodels.contribute

import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.data.TopicsRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributeViewModel @Inject constructor(
    val toolbar: ContributeToolbar,
    private val router: ContributeRouter,
    private val repository: TopicsRepository
) : BaseViewModel() {

    var title = ObservableField("")
    var opinionText = ObservableField("")

    private val _opinionState = MutableStateFlow(OpinionSortType.LOVE)
    val opinionType: StateFlow<OpinionSortType> = _opinionState

    fun onLoveClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionSortType.LOVE)
        }
    }

    fun onHateClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionSortType.HATE)
        }
    }

    fun onNeutralClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionSortType.INDIFFERENCE)
        }
    }

    fun onPublishClicked() {
        viewModelScope.launch {
            _isLoading.emit(true)

            repository.createTopic(
                title = title.get().orEmpty(),
                userId = 0,
                opinionType = opinionType.value,
                comment = opinionText.get().orEmpty()
            )?.let {
                router.navigateToNewTopic(it.id)

                delay(200) // Wait for animation
                _isLoading.emit(false)
            }
        }
    }
}