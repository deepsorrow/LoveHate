package com.kropotov.lovehate.ui.screens.profile

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kropotov.lovehate.data.repositories.UsersRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import com.kropotov.lovehate.ui.utilities.localize
import com.kropotov.lovehate.ui.utilities.plusServerIp
import com.kropotov.lovehate.workers.NotificationWorker
import com.kropotov.lovehate.workers.NotificationWorker.Companion.REPEAT_INTERVAL_IN_MINUTES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val applicationContext: Context,
    val resourceProvider: ResourceProvider,
    val toolbar: ProfileToolbar,
    val repository: UsersRepository,
    val sharedPrefs: SharedPreferencesHelper
) : BaseViewModel(resourceProvider) {

    val myUsername = ObservableField("")
    val myScoreTitle = ObservableField("")
    val myAvatarUrl = ObservableField("")

    val isNotificationsEnabled = sharedPrefs.getNotificationsEnabled()

    init {
        loadCurrentUserInfo()
    }

    fun onNotificationsSwitched(isChecked: Boolean) {
        sharedPrefs.saveNotificationEnabled(isChecked)
        if (isChecked) {
            scheduleNotificationWorker()
        } else {
            WorkManager.getInstance(applicationContext)
                .cancelUniqueWork(NotificationWorker::class.simpleName.orEmpty())
        }
    }

    private fun loadCurrentUserInfo() {
        viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
            repository.getCurrentUser()?.run {
                myUsername.set(username)

                val localizedScoreTitle = resourceProvider.getString(scoreTitle.localize())
                myScoreTitle.set("$localizedScoreTitle ($score)")
                myAvatarUrl.set(photoUrl.plusServerIp())
            }
        }
    }

    private fun scheduleNotificationWorker() {
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()
        val checkForNotificationsRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            REPEAT_INTERVAL_IN_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(constrains)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            NotificationWorker::class.simpleName.orEmpty(),
            ExistingPeriodicWorkPolicy.KEEP,
            checkForNotificationsRequest
        )
    }
}
