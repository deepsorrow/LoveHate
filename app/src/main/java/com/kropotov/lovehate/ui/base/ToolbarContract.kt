package com.kropotov.lovehate.ui.base

import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

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
    val isSubtitleVisible: ObservableBoolean

    val searchIconIsVisible: ObservableBoolean
    val searchText: ObservableField<String>

    val arrowBackIsVisible: ObservableBoolean
    val isBottomOffsetVisible: ObservableBoolean

    fun subscribeToSearchTextQuery(onTextChanged: (String) -> Unit) {
        searchText.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onTextChanged(searchText.get().orEmpty())
            }
        })
    }
}
