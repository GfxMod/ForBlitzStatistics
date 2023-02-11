package com.example.forblitzstatistics.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterfaceWG {

    @GET("wotb/account/list/?application_id=ac75820d6c10195c86370ec1bc9f21de")
    suspend fun getAccountId(@Query("search") search: String) : Response<ResponseBody>

    @GET("wotb/account/info/?application_id=ac75820d6c10195c86370ec1bc9f21de&extra=statistics.rating") //id_drnkwtr = 123297533 id_xomyak=95102841 id_light= 38687016
    suspend fun getUsers(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/clans/accountinfo/?application_id=ac75820d6c10195c86370ec1bc9f21de&extra=clan")
    suspend fun getClanInfo(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/clans/info/?application_id=ac75820d6c10195c86370ec1bc9f21de&extra=members") // было 218951
    suspend fun getFullClanInfo(@Query("clan_id") search: String) : Response<ResponseBody>

    @GET("wotb/account/achievements/?application_id=ac75820d6c10195c86370ec1bc9f21de&fields=-max_series")
    suspend fun getAchievements(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/encyclopedia/vehicles/?application_id=ac75820d6c10195c86370ec1bc9f21de&fields=name,nation,tier,type")
    suspend fun getAllInformationAboutVehicles() : Response<ResponseBody>

    @GET("wotb/tanks/stats/?application_id=ac75820d6c10195c86370ec1bc9f21de")
    suspend fun getTankStatistics(@Query("account_id") account_id: String, @Query("tank_id") search: String) : Response<ResponseBody>

}