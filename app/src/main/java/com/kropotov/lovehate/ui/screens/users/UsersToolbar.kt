package com.kropotov.lovehate.ui.screens.users

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.ui.base.ToolbarContract
import javax.inject.Inject

class UsersToolbar @Inject constructor(
    listType: UsersListType
) : ToolbarContract {

    override val toolbarVisibility = ObservableBoolean(true)
    override val toolbarColor = ObservableInt(0)

    override val title = ObservableField(listType.title)
    override val titleTextSize = ObservableField(R.dimen.toolbar_subtitle_text_size)
    override val subtitle = ObservableField(0)
    override val isSubtitleVisible = ObservableBoolean(false)

    override val searchIconIsVisible = ObservableBoolean(false)
    override val searchText = ObservableField("")
    override val arrowBackIsVisible = ObservableBoolean(true)
    override val isBottomOffsetVisible = ObservableBoolean(true)
}
