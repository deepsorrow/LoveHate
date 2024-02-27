package com.kropotov.lovehate.ui.vm.contribute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.data.OpinionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributeVm @Inject constructor() : ViewModel() {

    private val _opinionState = MutableStateFlow(OpinionType.LOVE)
    val opinionType: StateFlow<OpinionType> = _opinionState

    fun onLoveClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.LOVE)
        }
    }

    fun onHateClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.HATE)
        }
    }

    fun onNeutralClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.NEUTRAL)
        }
    }

    fun onPublishClicked() {

    }
}