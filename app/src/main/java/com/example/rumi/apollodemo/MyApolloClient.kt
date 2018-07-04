package com.example.rumi.apollodemo

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MyApolloClient {


    companion object {
        var BASE_URL="https://api.graph.cool/simple/v1/cjj6tzhbn03uu0154dveh01lz"
        lateinit var apolloClient: ApolloClient

        fun getMyAppolloClient():ApolloClient{
            var logginInterceptor=HttpLoggingInterceptor()
            logginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            var okHttpClient =OkHttpClient.Builder()
                    .addInterceptor(logginInterceptor)
                    .build()
            apolloClient = ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttpClient)
                    .build()
            return apolloClient
        }
    }

}