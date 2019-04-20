package com.ziggy.kdo.network.configuration

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.network.store.UserApi
import com.ziggy.kdo.ui.activity.login.LoginActivity
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RefreshTokenAuthenticator(
    private val httpBuilder: OkHttpClient.Builder,
    private val refreshToken: String,
    private val gson: Gson,
    private val context: Context
) : Authenticator {

    override fun authenticate(route: Route, response: Response): Request? {

        if (responseCount(response) >= 2) {
            UserSession.deleteUserSession(context)
            LoginActivity.newInstance(context)
            Log.e("CONTEXT", "EXIT")
            return null
        }
        val builder = response.request().newBuilder()

        httpBuilder.addInterceptor(AuthenticationInterceptor("bearer $refreshToken"))

        // execute synchronous call to request a new access token
        val service = retrofitBuilder(httpBuilder).create(UserApi::class.java)
        val call = service.postRefreshToken(refreshToken)
        val accessToken = call.execute().body()

        accessToken?.let { authToken ->

            UserSession.createUserToken(context, authToken.token!!, authToken.tokenRefresh!!)

            httpBuilder.addInterceptor(AuthenticationInterceptor("bearer ${authToken.token}"))

            builder.header("Authorization", "bearer ${authToken.token}")
        }

        return builder.build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1

        while ((response == response.priorResponse()) != null) {
            result++
            if (result == 2)
                break
        }

        return result
    }

    private fun retrofitBuilder(httpBuilder: OkHttpClient.Builder) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BuildConfig.ENDPOINT)
        .client(httpBuilder.build())
        .build()
}