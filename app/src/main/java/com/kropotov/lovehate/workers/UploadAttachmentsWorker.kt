package com.kropotov.lovehate.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.data.items.MediaListItem.Companion.toMultiparts
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UploadAttachmentsWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val opinionId = inputData.getString(OPINION_ID)
        val mediaList = inputData.getStringArray(ATTACHMENTS_PATHS_KEY)?.map {
            MediaListItem(filePath = it)
        }
        if (opinionId == null || mediaList == null) {
            val errorData = buildErrorData("OpinionId or mediaList is null!")
            return Result.failure(errorData)
        }

        val sharedPrefs = SharedPreferencesHelper(applicationContext)
        val response = BackendMainService
                .createMainService(sharedPrefs)
                .uploadOpinionAttachments(
                    opinionId = opinionId.toRequestBody("text/plain".toMediaTypeOrNull()),
                    files = mediaList.toMultiparts()
                ).execute()

        return if (response.isSuccessful) {
            Result.success()
        } else {
            val errorData = buildErrorData("${response.code()}: ${response.message()}")
            Result.failure(errorData)
        }
    }

    private fun buildErrorData(errorMessage: String) =
        Data.Builder().putString(ERROR_MESSAGE_KEY, errorMessage).build()

    companion object {
        const val OPINION_ID = "opinion_id"
        const val ATTACHMENTS_PATHS_KEY = "attachments_paths_key"
        const val ERROR_MESSAGE_KEY = "error_message_key"
    }
}