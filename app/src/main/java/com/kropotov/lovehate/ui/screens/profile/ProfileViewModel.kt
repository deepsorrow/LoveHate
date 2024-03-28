package com.kropotov.lovehate.ui.screens.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.data.repositories.UsersRepository
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import com.kropotov.lovehate.ui.utilities.localize
import com.kropotov.lovehate.ui.utilities.plusServerIp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
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
    }

    private fun loadCurrentUserInfo() {
        viewModelScope.launch(Dispatchers.IO + defaultExceptionHandler) {
            repository.getCurrentUser()?.run {
                myUsername.set(username)

                val localizedScoreTitle = resourceProvider.getString(scoreTitle.localize())
                val scoreTitle = String.format(Locale.ROOT, "%1s (%2s)", localizedScoreTitle, score)
                myScoreTitle.set(scoreTitle)
                myAvatarUrl.set(photoUrl.plusServerIp())
            }
        }
    }
}
