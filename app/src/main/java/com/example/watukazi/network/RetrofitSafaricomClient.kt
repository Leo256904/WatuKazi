package com.watukazi.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.watukazi.app.util.Constants


object RetrofitSafaricomClient {
    val api: SafaricomApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SafaricomApi::class.java)
    }
}