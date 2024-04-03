package com.kropotov.lovehate.ui.screens.attachmentviewer

import android.graphics.Bitmap
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import javax.inject.Inject

class AttachmentViewerViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val imageUrl: String?,
    val thumbnailBitmap: Bitmap?,
) : BaseViewModel(resourceProvider) {

    val title: String = imageUrl?.substringAfterLast("/").orEmpty()

}