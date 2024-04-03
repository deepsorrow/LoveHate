package com.kropotov.lovehate.data.items

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Parcelize
data class MediaListItem(
    val uri: Uri? = null,
    var filePath: String? = null,
    val id: Long = 0
) : Parcelable {

    val isEmpty
        get() = uri?.path.orEmpty().isEmpty()

    companion object {
        fun List<MediaListItem>.toMultiparts() = buildList {
            this@toMultiparts.forEachIndexed { index, media ->
                media.filePath?.let { filePath ->
                    val file = File(filePath)
                    add(
                        MultipartBody.Part.createFormData(
                            "file$index",
                            file.name,
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        )
                    )
                }
            }
        }
    }
}
