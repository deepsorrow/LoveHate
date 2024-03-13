package com.kropotov.lovehate.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

interface BackendService {

    companion object {
        private const val BASE_URL = "172.16.0.98:1337"

        fun create(): ApolloClient = ApolloClient.Builder()
            .serverUrl("http://$BASE_URL/api/v1/")
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor())
                    .build()
            )
            .build()
    }

    private class AuthorizationInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("x-love-hate-secret", "<key>")
                .build()

            return chain.proceed(request)
        }
    }
}