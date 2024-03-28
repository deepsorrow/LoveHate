package com.kropotov.lovehate.ui.screens.topicpage

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.data.OpinionType
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
import javax.inject.Inject
import javax.inject.Named

class NewOpinionViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    @Named(TOPIC_PAGE_ID) val topicId: Int,
    private val apolloClient: ApolloClient
) : BaseViewModel(resourceProvider), OpinionStateListener {

    val text = ObservableField("")
    var mediaPaths = mutableListOf<String>()

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
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            apolloClient.mutation(
                OpinionsQueryAdapter.publishOpinion(
                    topicId,
                    text.get().orEmpty(),
                    opinionState.value
                )
            ).execute()

            _isLoading.emit(false)
            _dismissDialog.emit(Unit)
        }
    }
}
