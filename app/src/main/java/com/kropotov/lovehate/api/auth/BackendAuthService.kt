package com.kropotov.lovehate.api.auth

import com.kropotov.lovehate.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendAuthService {

    @POST("login")
    fun login(@Body credentials: UserCredentials): Call<TokenResponse>

    @POST("register")
    fun register(@Body credentials: UserCredentials): Call<TokenResponse>

    @GET("isUsernameOccupied")
    fun isUsernameOccupied(@Query("username") username: String): Call<Boolean>

    companion object {

        fun create(): BackendAuthService {
            val serverIp = BuildConfig.SERVER_IP
            val client = OkHttpClient
                .Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl(serverIp)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackendAuthService::class.java)
        }
    }
}
