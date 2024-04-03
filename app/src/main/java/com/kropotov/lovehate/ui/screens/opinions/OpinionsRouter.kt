package com.kropotov.lovehate.ui.screens.opinions

import android.graphics.Bitmap
import androidx.fragment.app.commit
import com.kropotov.lovehate.R
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment
import com.kropotov.lovehate.ui.screens.opinions.fragments.OpinionsFragment
import javax.inject.Inject

class OpinionsRouter @Inject constructor(
    val fragment: OpinionsFragment
) : BaseRouter(fragment.parentFragmentManager) {

    fun navigateToAttachmentViewer(thumbnailBitmap: Bitmap, url: String) {
        val viewerFragment = AttachmentViewerFragment.newInstance(thumbnailBitmap, url)
        fragment.requireActivity().supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down,
                0,
                R.anim.slide_down
            )
            add(R.id.main_overlay_container, viewerFragment)
            addToBackStack(null)
        }
    }
}