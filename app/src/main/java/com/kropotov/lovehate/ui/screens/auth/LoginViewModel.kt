package com.kropotov.lovehate.ui.screens.auth

import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.api.auth.BackendAuthService
import com.kropotov.lovehate.ui.screens.auth.base.BaseAuthViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    sharedPrefs: SharedPreferencesHelper,
    private val service: BackendAuthService
) : BaseAuthViewModel(resourceProvider, sharedPrefs) {

    fun login() {
        viewModelScope.launch {
            !checkLogin() && return@launch
            !checkLengthPassword() && return@launch

            _isLoading.emit(true)
            withContext(Dispatchers.IO) {
                service.login(getCredentials()).enqueue(serviceCallbackResponse)
            }
        }
    }
}