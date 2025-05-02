package com.watukazi.app.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Headers
import retrofit2.http.Part
import retrofit2.Call

interface ImgurApiService {
    @Headers("Authorization: Client-ID c8f9edab89452f8")
    @Multipart
    @POST("3/image")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<ImgurResponse>
}


