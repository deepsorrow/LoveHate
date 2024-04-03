package com.kropotov.lovehate.di.mainscreen.fragments

import android.graphics.Bitmap
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment.Companion.IMAGE_URL_KEY
import com.kropotov.lovehate.ui.screens.attachmentviewer.AttachmentViewerFragment.Companion.THUMBNAIL_BITMAP_KEY
import com.kropotov.lovehate.ui.utilities.parcelable
import dagger.Module
import dagger.Provides

@Module
class AttachmentViewerFragmentModule {

    @Provides
    fun provideImageUrl(fragment: AttachmentViewerFragment): String?
        = fragment.arguments?.getString(IMAGE_URL_KEY)

    @Provides
    fun provideThumbnailBitmap(fragment: AttachmentViewerFragment)
        = fragment.arguments?.parcelable<Bitmap>(THUMBNAIL_BITMAP_KEY)
}
