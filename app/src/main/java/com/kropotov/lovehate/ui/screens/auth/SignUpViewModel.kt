package com.kropotov.lovehate.ui.screens.auth

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.auth.BackendAuthService
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.screens.auth.base.BaseAuthViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val sharedPrefs: SharedPreferencesHelper,
    private val service: BackendAuthService
): BaseAuthViewModel(resourceProvider, sharedPrefs) {

    val confirmPassword = ObservableField("")

    val validationNameIsVisible = ObservableBoolean(false)
    val validationNameText = ObservableInt(R.string.the_name_is_already_taken)
    val validationNameTextColor = ObservableInt(R.attr.hate_color)

    val validationPasswordIsVisible = ObservableBoolean(false)
    val validationPasswordConfirmIsVisible = ObservableBoolean(false)

    fun register() {
        viewModelScope.launch {
            !checkLogin() && return@launch
            !checkLengthPassword() && return@launch
            !checkConfirmPassword() && return@launch

            _isLoading.emit(true)
            withContext(Dispatchers.IO) {
                service.register(getCredentials()).enqueue(serviceCallbackResponse)
            }
        }
    }

    suspend fun validateIsNameTaken() {
        if (!checkLoginLength()) {
            validationNameIsVisible.set(false)
            return
        }

        withContext(Dispatchers.IO) {
            try {
                service.isUsernameOccupied(login.get().orEmpty()).execute().body().let { isTaken ->
                    if (isTaken == null) {
                        validationNameIsVisible.set(false)
                        return@let
                    }

                    validationNameIsVisible.set(true)
                    if (isTaken) {
                        validationNameTextColor.set(R.attr.hate_color)
                        validationNameText.set(R.string.the_name_is_already_taken)
                    } else {
                        validationNameTextColor.set(R.attr.love_color)
                        validationNameText.set(R.string.the_name_is_available)
                    }
                }
            } catch (e: Exception) {
                emitMessage(R.string.unknown_error, InformType.ERROR, e.message.orEmpty())
            }
        }
    }

    fun validateConfirmPassword() {
        val passwordNotMatches = password.get() != confirmPassword.get()
        validationPasswordConfirmIsVisible.set(passwordNotMatches)
    }

    fun validateMinLengthPassword() {
        val passwordLength = (password.get()?.length) ?: 0
        validationPasswordIsVisible.set(passwordLength < PASSWORD_MIN_LENGTH)
    }

    private suspend fun checkConfirmPassword() =
        if (validationPasswordConfirmIsVisible.get()) {
            emitMessage(R.string.passwords_match_error, InformType.ERROR)
            false
        } else {
            true
        }

    companion object {
        const val CHECK_USERNAME_DEBOUNCE = 400L
    }
}