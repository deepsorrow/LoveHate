package com.kropotov.lovehate.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ContributeOpinionWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        TODO("Background attachments upload feature")
    }
}