package com.kropotov.lovehate.api.main

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.kropotov.lovehate.BuildConfig
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BackendMainService {

    @Multipart
    @POST("uploadOpinionAttachments")
    fun uploadOpinionAttachments(
        @Part("id") opinionId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Call<Unit>

    companion object {

        fun createApolloClient(sharedPrefs: SharedPreferencesHelper): ApolloClient {
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

        /**
         * Creates main service instance. If [sharedPrefs] is null, i.e. invoked from worker,
         * then token interceptor would not be added.
         */
        fun createMainService(sharedPrefs: SharedPreferencesHelper?): BackendMainService {
            val serverIp = BuildConfig.SERVER_IP
            val token = sharedPrefs?.getToken()

            val client = if (sharedPrefs != null) {
                OkHttpClient
                    .Builder()
                    .addInterceptor(AuthorizationInterceptor(token))
                    .build()
            } else {
                OkHttpClient.Builder().build()
            }

            return Retrofit.Builder()
                .baseUrl(serverIp)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackendMainService::class.java)
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
