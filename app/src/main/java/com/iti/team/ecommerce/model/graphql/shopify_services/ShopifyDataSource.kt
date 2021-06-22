package com.iti.team.ecommerce.model.graphql.shopify_services

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.iti.team.ecommerce.BuildConfig
import com.iti.team.ecommerce.RequestAllOrdersQuery
import com.iti.team.ecommerce.model.remote.NetworkService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class ShopifyDataSource:ShopifyServices {
    companion object {
        private var INSTANCE: ShopifyDataSource? = null
        private lateinit var apolloClient: ApolloClient

        @JvmStatic
        fun getInstance(): ShopifyDataSource = INSTANCE ?: build()

        @JvmStatic
        private fun build(): ShopifyDataSource {
            apolloClient = ApolloClient.builder()
                .serverUrl("https://ce751b18c7156bf720ea405ad19614f4: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@itiana.myshopify.com/admin/api/2021-04/graphql.json")
                .okHttpClient(buildAuthClient())
                .build()
            INSTANCE = ShopifyDataSource()
            return INSTANCE!!
        }
        @JvmStatic
        private fun buildAuthClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(provideLoggingInterceptor())
            httpClient.addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("authorization","S9JNTDT2HB-JBRGLTGWTH-9ZKWJWWWBK")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Shopify-Access-Token","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")
                    .build()
                chain.proceed(newRequest)
            }

            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.readTimeout(60, TimeUnit.SECONDS)
            return httpClient.build()
        }
        @JvmStatic
        private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
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


    override suspend fun getOrders(email: String): Response<RequestAllOrdersQuery.Data> {
        return apolloClient.query(RequestAllOrdersQuery(email = Input.fromNullable(email))).await()
    }

}