package ru.forblitz.statistics.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterfaceForBlitz {

    @GET("wp-json/wp/v2/appinfo")
    suspend fun getAll() : Response<ResponseBody>

}