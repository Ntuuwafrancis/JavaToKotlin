package com.sriyank.javatokotlindemo.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // create Base Url
    private const val BASE_URL = "https://api.github.com/"

    //	Create Logger
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    //	Create OkHttp Client
    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(logger)

    // Create retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build())
                .build()
    }

    // Create retrofit service
    val githubAPIService: GithubAPIService by lazy {
        retrofit.create(GithubAPIService::class.java)
    }

}