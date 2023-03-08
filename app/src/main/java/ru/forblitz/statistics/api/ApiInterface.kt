package ru.forblitz.statistics.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @GET
    suspend fun getAccountId(@Url url: String, @Query("search") search: String) : Response<ResponseBody>

    @GET
    suspend fun getUsers(@Url url: String, @Query("account_id") search: String) : Response<ResponseBody>

    @GET
    suspend fun getClanInfo(@Url url: String, @Query("account_id") search: String) : Response<ResponseBody>

    @GET
    suspend fun getFullClanInfo(@Url url: String, @Query("clan_id") search: String) : Response<ResponseBody>

    @GET
    suspend fun getAchievements(@Url url: String, @Query("account_id") search: String) : Response<ResponseBody>

    @GET
    suspend fun getAllInformationAboutVehicles(@Url url: String) : Response<ResponseBody>

    @GET
    suspend fun getTankStatistics(@Url url: String, @Query("account_id") accountId: String, @Query("tank_id") tankId: String) : Response<ResponseBody>

}