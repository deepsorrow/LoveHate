package com.kropotov.lovehate.api.main

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.kropotov.lovehate.BuildConfig
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

interface BackendMainService {

    companion object {
        fun create(sharedPrefs: SharedPreferencesHelper): ApolloClient {
            val serverUrl = BuildConfig.MAIN_SERVICE_URL
            val token = sharedPrefs.getToken()

            return ApolloClient.Builder()
                .serverUrl(serverUrl)
                .okHttpClient(
                    OkHttpClient.Builder()
                        .addInterceptor(AuthorizationInterceptor(token))
                        .build()
                )
                .build()
        }
    }

    private class AuthorizationInterceptor(val token: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            return chain.proceed(request)
        }
    }
}
