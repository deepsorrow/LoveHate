package com.kropotov.lovehate.ui.screens.opinions

import android.graphics.Bitmap
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment
import javax.inject.Inject

class OpinionsRouter @Inject constructor(
    val fragment: OpinionsFragment
) : BaseRouter(fragment.parentFragmentManager) {

    fun navigateToAttachmentViewer(thumbnailBitmap: Bitmap, url: String) =
        navigateWithSlideUpTransition(
            fragment = AttachmentViewerFragment.newInstance(thumbnailBitmap, url),
            usedFragmentManager = fragment.requireActivity().supportFragmentManager,
            container = R.id.main_overlay_container
        )
}
