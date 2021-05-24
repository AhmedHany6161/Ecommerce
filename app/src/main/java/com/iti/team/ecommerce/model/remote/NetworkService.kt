package com.iti.team.ecommerce.model.remote

import com.iti.team.ecommerce.BuildConfig
import com.iti.team.ecommerce.utils.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkService {
    private lateinit var retrofit: Retrofit
    fun createRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildAuthClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                .add(
                    KotlinJsonAdapterFactory()
                ).build()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return retrofit
    }

    private fun buildAuthClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(provideLoggingInterceptor())
        httpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Shopify-Access-Token","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")
                .build()
            chain.proceed(newRequest)
        }

        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        return httpClient.build()
    }

    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()

        @Suppress("ConstantConditionIf")
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

}