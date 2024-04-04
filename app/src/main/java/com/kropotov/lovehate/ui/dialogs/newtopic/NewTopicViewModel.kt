package com.kropotov.lovehate.ui.dialogs.newtopic

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.data.items.MediaListItem.Companion.toMultiparts
import com.kropotov.lovehate.data.repositories.TopicsRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.dialogs.newopinion.NewOpinionViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.views.LoveHateTextField.OpinionStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NewTopicViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val repository: TopicsRepository,
    private val service: BackendMainService
) : BaseViewModel(resourceProvider), OpinionStateListener {

    var title = ObservableField("")
    var opinion = ObservableField("")
    var mediaPaths = mutableListOf(MediaListItem())

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
        viewModelScope.launch(defaultExceptionHandler) {
            if (title.get()!!.length < MINIMUM_TOPIC_LENGTH) {
                emitMessage(R.string.at_least_3_characters_is_required, InformType.ERROR)
                return@launch
            } else if (opinion.get()!!.length < NewOpinionViewModel.MINIMUM_OPINION_LENGTH) {
                emitMessage(R.string.at_least_20_characters_is_required, InformType.ERROR)
                return@launch
            } else if (mediaPaths.none { !it.isEmpty }) {
                emitMessage(R.string.at_least_1_photo, InformType.ERROR)
                return@launch
            }
            _isLoading.emit(true)

            withContext(Dispatchers.IO) {
                repository.createTopic(
                    title = title.get().orEmpty(),
                    opinionType = opinionType.value,
                    comment = opinion.get().orEmpty()
                ).let {
                    val (topicId, opinionId) = it

                    val response = sendAttachments(opinionId)
                    _isLoading.emit(false)

                    if (response.isSuccessful) {
                        _navigateToNewTopic.emit(topicId)
                    } else {
                        emitMessage(
                            R.string.unknown_error,
                            InformType.ERROR,
                            "code ${response.code()}, ${response.message()}"
                        )
                    }
                }
            }
        }
    }

    private fun sendAttachments(id: Int) =
        service.uploadOpinionAttachments(
            opinionId = id.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            files = mediaPaths.toMultiparts()
        ).execute()

    companion object {
        const val MINIMUM_TOPIC_LENGTH = 3
    }
}
