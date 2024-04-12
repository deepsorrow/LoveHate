package com.kropotov.lovehate.ui.screens.attachmentviewer

import android.graphics.Bitmap
import android.os.Bundle
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentAttachmentViewerBinding
import com.kropotov.lovehate.ui.MainScreenActivity
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.withArgs

class AttachmentViewerFragment :
    BaseFragment<AttachmentViewerViewModel, FragmentAttachmentViewerBinding>(R.layout.fragment_attachment_viewer) {

    override val vmClass = AttachmentViewerViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainScreenActivity).hideUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainScreenActivity).showUI()
    }

    companion object {
        const val THUMBNAIL_BITMAP_KEY = "thumbnail_bitmap_key"
        const val IMAGE_URL_KEY = "image_url_key"

        fun newInstance(bitmap: Bitmap?, url: String?) = AttachmentViewerFragment().withArgs {
            putParcelable(THUMBNAIL_BITMAP_KEY, bitmap)
            putString(IMAGE_URL_KEY, url)
        }
    }
}