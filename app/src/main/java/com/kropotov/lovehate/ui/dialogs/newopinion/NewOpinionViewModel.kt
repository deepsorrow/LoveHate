package com.kropotov.lovehate.ui.dialogs.newopinion

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.views.LoveHateTextField.OpinionStateListener
import com.kropotov.lovehate.workers.UploadAttachmentsWorker
import com.kropotov.lovehate.workers.UploadAttachmentsWorker.Companion.ATTACHMENTS_PATHS_KEY
import com.kropotov.lovehate.workers.UploadAttachmentsWorker.Companion.ERROR_MESSAGE_KEY
import com.kropotov.lovehate.workers.UploadAttachmentsWorker.Companion.OPINION_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class NewOpinionViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val applicationContext: Context,
    @Named(TOPIC_PAGE_ID) val topicId: Int,
    private val apolloClient: ApolloClient,
    private val service: BackendMainService
) : BaseViewModel(resourceProvider), OpinionStateListener {

    val text = ObservableField("")
    var mediaPaths = mutableListOf(MediaListItem())

    private val _opinionState = MutableStateFlow(OpinionType.LOVE)
    val opinionState: StateFlow<OpinionType> = _opinionState

    private val _navigateToNewOpinion = MutableSharedFlow<Unit>()
    val navigateToNewOpinion: SharedFlow<Unit> = _navigateToNewOpinion

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

            if (mediaPaths.none { !it.isEmpty }) {
                runUploadAttachmentsWorker(opinionId).collect {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        _isLoading.emit(false)
                        _navigateToNewOpinion.emit(Unit)
                    } else if (it.state == WorkInfo.State.FAILED) {
                        emitMessage(
                            R.string.unknown_error,
                            InformType.ERROR,
                            it.outputData.getString(ERROR_MESSAGE_KEY) ?: ""
                        )
                    }
                }
            } else {
                _isLoading.emit(false)
                _navigateToNewOpinion.emit(Unit)
            }
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

    private fun runUploadAttachmentsWorker(id: Int): Flow<WorkInfo> {
        val inputData = Data.Builder()
            .putString(OPINION_ID, id.toString())
            .putStringArray(
                ATTACHMENTS_PATHS_KEY,
                mediaPaths.filter { !it.isEmpty }.map { it.filePath }.toTypedArray()
            ).build()

        val request = OneTimeWorkRequestBuilder<UploadAttachmentsWorker>()
            .setInputData(inputData)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(request)

        return workManager.getWorkInfoByIdFlow(request.id)
    }

    companion object {
        const val MAX_MEDIA_COUNT = 4
        const val MINIMUM_OPINION_LENGTH = 15
    }
}
