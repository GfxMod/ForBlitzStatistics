package com.example.forblitzstatistics.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterfaceVersion {

    @GET("wp-json/wp/v2/appinfo")
    suspend fun getCurrent() : Response<ResponseBody>

}