package com.kropotov.lovehate.ui.dialogs.newopinion

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.data.items.MediaListItem.Companion.toMultiparts
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.views.LoveHateTextField.OpinionStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

class NewOpinionViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    @Named(TOPIC_PAGE_ID) val topicId: Int,
    private val apolloClient: ApolloClient,
    private val service: BackendMainService
) : BaseViewModel(resourceProvider), OpinionStateListener {

    val text = ObservableField("")
    var mediaPaths = mutableListOf(MediaListItem())

    private val _opinionState = MutableStateFlow(OpinionType.LOVE)
    val opinionState: StateFlow<OpinionType> = _opinionState

    private val _dismissDialog = MutableSharedFlow<Unit>()
    val dismissDialog: SharedFlow<Unit> = _dismissDialog

    override fun onLoveClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.LOVE)
        }
    }

    override fun onNeutralClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.INDIFFERENCE)
        }
    }

    override fun onHateClicked() {
        viewModelScope.launch {
            _opinionState.emit(OpinionType.HATE)
        }
    }

    fun onSendClicked() {
        viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
            if (text.get()!!.length < MINIMUM_OPINION_LENGTH) {
                emitMessage(R.string.at_least_20_characters_is_required, InformType.ERROR)
                return@launch
            }

            _isLoading.emit(true)

            val opinionId = sendOpinion()
            sendAttachments(opinionId)

            _isLoading.emit(false)
            _dismissDialog.emit(Unit)
        }
    }

    private suspend fun sendOpinion() =
        apolloClient.mutation(
            OpinionsQueryAdapter.publishOpinion(
                topicId,
                text.get().orEmpty(),
                opinionState.value
            )
        ).execute().dataAssertNoErrors.publishOpinion!!.opinion.id

    private fun sendAttachments(id: Int) {
        service.uploadOpinionAttachments(
            opinionId = id.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            files = mediaPaths.toMultiparts()
        ).execute()
    }

    companion object {
        const val MAX_MEDIA_COUNT = 4
        const val MINIMUM_OPINION_LENGTH = 25
    }
}
