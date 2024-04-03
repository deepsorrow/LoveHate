package com.kropotov.lovehate.ui.screens.auth.base

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.api.auth.TokenResponse
import com.kropotov.lovehate.api.auth.UserCredentials
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.base.BaseViewModel
import com.kropotov.lovehate.ui.utilities.ResourceProvider
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseAuthViewModel(
    resourceProvider: ResourceProvider,
    private val sharedPrefs: SharedPreferencesHelper
): BaseViewModel(resourceProvider) {

    val login = ObservableField("")
    val password = ObservableField("")

    private val _navigateToMainScreen = MutableSharedFlow<Unit>()
    val navigateToMainScreen: SharedFlow<Unit> = _navigateToMainScreen

    protected val serviceCallbackResponse = object : Callback<TokenResponse> {
        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
            viewModelScope.launch {
                _isLoading.emit(false)

                val token = response.body()?.token
                if (response.code() == CONFLICT_CODE) {
                    emitMessage(R.string.the_name_is_already_taken_error, InformType.ERROR)
                } else if  (response.code() == UNAUTHORIZED_CODE) {
                    emitMessage(R.string.wrong_username_or_password_error, InformType.ERROR)
                } else if (token.isNullOrEmpty()) {
                    emitMessage(R.string.invalid_token_error, InformType.ERROR)
                } else {
                    sharedPrefs.saveToken(token)
                    _navigateToMainScreen.emit(Unit)
                }
            }
        }

        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
            viewModelScope.launch {
                emitMessage(R.string.unknown_error, InformType.ERROR, t.message.orEmpty())
                _isLoading.emit(false)
            }
        }
    }

    protected suspend fun checkLogin() =
        if (checkLoginLength()) {
            true
        } else {
            emitMessage(R.string.login_error, InformType.ERROR)
            false
        }

    protected suspend fun checkLengthPassword() =
        if ((password.get()?.length ?: 0) < PASSWORD_MIN_LENGTH) {
            emitMessage(R.string.password_error, InformType.ERROR)
            false
        } else {
            true
        }

    protected fun checkLoginLength() = (login.get()?.length ?: 0) >= LOGIN_MIN_LENGTH

    protected fun getCredentials() = UserCredentials(login.get().orEmpty(), password.get().orEmpty())

    companion object {
        const val UNAUTHORIZED_CODE = 401
        const val PASSWORD_MIN_LENGTH = 6
        private const val CONFLICT_CODE = 409
        private const val LOGIN_MIN_LENGTH = 4
    }
}