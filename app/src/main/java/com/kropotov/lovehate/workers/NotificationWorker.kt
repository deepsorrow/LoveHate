package com.kropotov.lovehate.workers

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.main.BackendMainService
import com.kropotov.lovehate.api.main.OpinionsQueryAdapter
import com.kropotov.lovehate.type.ReactionType
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import kotlin.random.Random

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val sharedPrefs = SharedPreferencesHelper(applicationContext)
        (!sharedPrefs.getNotificationsEnabled()) && return Result.failure()

        val notifications = BackendMainService
            .createApolloClient(sharedPrefs)
            .query(OpinionsQueryAdapter.getNotifications())
            .execute()
            .dataAssertNoErrors
            .notifications
            .map { it.notificationReaction }
        notifications.isEmpty() && return Result.success()

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notifications.forEach {
            val title = if (it.type == ReactionType.LIKE) {
                R.string.notification_like_reaction
            } else {
                R.string.notification_dislike_reaction
            }.run {
                applicationContext.getString(this, it.who)
            }

            val builder = NotificationCompat.Builder(
                applicationContext,
                NOTIFICATION_CHANNEL_ID

            )   .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(it.sourceText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val notificationId = Random.nextInt(10000)
                notificationManager.notify(notificationId, builder.build())
            }
        }
        return Result.success()
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "12345"
        const val REPEAT_INTERVAL_IN_MINUTES = 30L
    }
}
