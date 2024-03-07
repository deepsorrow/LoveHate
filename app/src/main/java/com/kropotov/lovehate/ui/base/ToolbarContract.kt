package com.kropotov.lovehate.ui.base

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @property toolbarColor toolbar attribute color
 * @property title toolbar title string resource
 * @property subtitle toolbar subtitle string resource
 */
interface ToolbarContract {

    val toolbarVisibility: ObservableBoolean
    val toolbarColor: ObservableInt

    val title: ObservableField<Int>
    val subtitle: ObservableField<Int>
    val subtitleIsVisible: ObservableBoolean

    val searchIconIsVisible: ObservableBoolean
    val searchText: MutableStateFlow<String>

    val arrowBackIsVisible: ObservableBoolean
    val isBottomOffsetVisible: ObservableBoolean

}