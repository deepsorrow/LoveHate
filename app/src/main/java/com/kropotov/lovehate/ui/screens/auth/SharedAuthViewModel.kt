package com.kropotov.lovehate.ui.screens.auth

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.auth.BackendAuthService
import com.kropotov.lovehate.api.auth.TokenResponse
import com.kropotov.lovehate.api.auth.UserCredentials
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SharedAuthViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    private val sharedPrefs: SharedPreferencesHelper,
    private val service: BackendAuthService
): BaseViewModel(resourceProvider) {

    val login = ObservableField("")
    val password = ObservableField("")
    val confirmPassword = ObservableField("")

    private val _navigateToMainScreen = MutableSharedFlow<Unit>()
    val navigateToMainScreen: SharedFlow<Unit> = _navigateToMainScreen

    val validationNameIsVisible = ObservableBoolean(false)
    val validationNameText = ObservableInt(R.string.the_name_is_already_taken)
    val validationNameTextColor = ObservableInt(R.attr.hate_color)

    val validationPasswordIsVisible = ObservableBoolean(false)
    val validationPasswordConfirmIsVisible = ObservableBoolean(false)

    private val serviceCallbackResponse = object : Callback<TokenResponse> {
        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
            viewModelScope.launch {
                _isLoading.emit(false)

                val token = response.body()?.token
                if (response.code() == CONFLICT_CODE) {
                    emitErrorMessage(R.string.the_name_is_already_taken_error)
                } else if (response.code() == UNAUTHORIZED_CODE) {
                    emitErrorMessage(R.string.wrong_username_or_password_error)
                } else if (token.isNullOrEmpty()) {
                    emitErrorMessage(R.string.invalid_token_error)
                } else {
                    sharedPrefs.saveToken(token)
                    _navigateToMainScreen.emit(Unit)
                }
            }
        }

        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
            viewModelScope.launch {
                emitErrorMessage(R.string.unknown_error, t.message.orEmpty())
                _isLoading.emit(false)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            !checkLogin() && return@launch
            !checkLengthPassword() && return@launch

            withContext(Dispatchers.IO) {
                _isLoading.emit(true)
                service.login(getCredentials()).enqueue(serviceCallbackResponse)
            }
        }
    }

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

    fun validateMinLengthPassword() {
        val passwordLength = (password.get()?.length) ?: 0
        validationPasswordIsVisible.set(passwordLength < PASSWORD_MIN_LENGTH)
    }

    fun validateConfirmPassword() {
        val passwordNotMatches = password.get() != confirmPassword.get()
        validationPasswordConfirmIsVisible.set(passwordNotMatches)
    }

    suspend fun validateIsNameTaken() {
        if (!checkLoginLength()) {
            validationNameIsVisible.set(false)
            return
        }

        withContext(Dispatchers.IO) {
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
        }
    }

    private suspend fun checkLogin() =
        if (checkLoginLength()) {
            true
        } else {
            emitErrorMessage(R.string.login_error)
            false
        }

    private suspend fun checkLengthPassword() =
        if ((password.get()?.length ?: 0) < PASSWORD_MIN_LENGTH) {
            emitErrorMessage(R.string.password_error)
            false
        } else {
            true
        }

    private suspend fun checkConfirmPassword() =
        if (validationPasswordConfirmIsVisible.get()) {
            emitErrorMessage(R.string.passwords_match_error)
            false
        } else {
            true
        }

    private fun checkLoginLength() = (login.get()?.length ?: 0) >= LOGIN_MIN_LENGTH

    private fun getCredentials() = UserCredentials(login.get().orEmpty(), password.get().orEmpty())

    companion object {

        const val CHECK_USERNAME_DEBOUNCE = 400L
        private const val LOGIN_MIN_LENGTH = 4
        private const val PASSWORD_MIN_LENGTH = 6

        private const val UNAUTHORIZED_CODE = 401
        private const val CONFLICT_CODE = 409
    }
}
