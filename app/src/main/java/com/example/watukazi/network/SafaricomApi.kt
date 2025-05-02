package com.watukazi.app.network

import com.watukazi.app.daraja.STKPushRequest
import com.watukazi.app.models.*
import retrofit2.Call
import retrofit2.http.*

interface SafaricomApi {

    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessToken(
        @Header("Authorization") auth: String
    ): Call<AccessTokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    fun initiateStkPush(
        @Header("Authorization") auth: String,
        @Body request: STKPushRequest
    ): Call<STKPushResponse>
}


