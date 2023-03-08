package ru.forblitz.statistics.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterfaceLesta {

    // TODO: вынести идентификатор приложения в отдельную переменную (константу - public static final) 
    // в какой-нибудь отдельный класс, где будут храниться настройки приложения

    @GET("wotb/account/list/?application_id=1957e1f71656310342971b3b1aa2efef")
    suspend fun getAccountId(@Query("search") search: String) : Response<ResponseBody>

    @GET("wotb/account/info/?application_id=1957e1f71656310342971b3b1aa2efef&extra=statistics.rating") //id_drnkwtr = 123297533 id_xomyak=95102841 id_light= 38687016
    suspend fun getUsers(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/clans/accountinfo/?application_id=1957e1f71656310342971b3b1aa2efef&extra=clan")
    suspend fun getClanInfo(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/clans/info/?application_id=1957e1f71656310342971b3b1aa2efef&extra=members") // было 218951
    suspend fun getFullClanInfo(@Query("clan_id") search: String) : Response<ResponseBody>

    @GET("wotb/account/achievements/?application_id=1957e1f71656310342971b3b1aa2efef&fields=-max_series")
    suspend fun getAchievements(@Query("account_id") search: String) : Response<ResponseBody>

    @GET("wotb/encyclopedia/vehicles/?application_id=1957e1f71656310342971b3b1aa2efef&fields=name,nation,tier,type")
    suspend fun getAllInformationAboutVehicles() : Response<ResponseBody>

    @GET("wotb/tanks/stats/?application_id=1957e1f71656310342971b3b1aa2efef")
    suspend fun getTankStatistics(@Query("account_id") account_id: String, @Query("tank_id") search: String) : Response<ResponseBody>

}