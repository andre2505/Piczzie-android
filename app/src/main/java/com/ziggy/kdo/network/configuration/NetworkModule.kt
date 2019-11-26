package com.ziggy.kdo.network.configuration


import android.app.Application
import android.text.TextUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ziggy.kdo.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)

        return client
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient.Builder, application: Application): Retrofit {

        val retrofitBuilder = Retrofit.Builder()
        val session = UserSession.getUserToken(application)
        val refreshToken = UserSession.getUserRefreshToken(application)

        if (session!!.isNotEmpty()) {

            val interceptor = AuthenticationInterceptor(session)
            val authorization = RefreshTokenAuthenticator(okHttpClient, refreshToken!!, gson, application)
            if (!okHttpClient.interceptors().contains(interceptor)) {
                okHttpClient.addInterceptor(interceptor)
            }
            okHttpClient.authenticator(authorization)
        }

        return retrofitBuilder
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BuildConfig.ENDPOINT)
            .client(okHttpClient.build())
            .build()
    }
}