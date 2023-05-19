package ru.forblitz.statistics.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import ru.forblitz.statistics.data.Constants.*
import ru.forblitz.statistics.service.ConnectivityService

class ApiService(private val connectivityService: ConnectivityService) {

    private lateinit var region: String
    private lateinit var apiInterface: ApiInterface

    fun setRegion(region: String): ApiInterface {
        this.region = region
        apiInterface = Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(
                NetworkConnectionInterceptor(connectivityService)
            ).build())
            .baseUrl(baseUrl[region].toString())
            .build().create(ApiInterface::class.java)
        return apiInterface
    }

    suspend fun getAccountId(search: String): Response<ResponseBody> {
        return apiInterface.getAccountId(url["getAccountId"].toString().replace("APP_ID", apiId[region].toString()), search)
    }

    suspend fun getUsers(search: String): Response<ResponseBody> {
        return apiInterface.getUsers(url["getUsers"].toString().replace("APP_ID", apiId[region].toString()), search)
    }

    suspend fun getClanInfo(search: String): Response<ResponseBody> {
        return apiInterface.getClanInfo(url["getClanInfo"].toString().replace("APP_ID", apiId[region].toString()), search)
    }

    suspend fun getFullClanInfo(search: String): Response<ResponseBody> {
        return apiInterface.getFullClanInfo(url["getFullClanInfo"].toString().replace("APP_ID", apiId[region].toString()), search)
    }

    suspend fun getAchievements(search: String): Response<ResponseBody> {
        return apiInterface.getAchievements(url["getAchievements"].toString().replace("APP_ID", apiId[region].toString()), search)
    }

    suspend fun getAllInformationAboutVehicles(): Response<ResponseBody> {
        return apiInterface.getAllInformationAboutVehicles(url["getAllInformationAboutVehicles"].toString().replace("APP_ID", apiId[region].toString()))
    }

    suspend fun getTankStatistics(account_id: String, search: String): Response<ResponseBody> {
        return apiInterface.getTankStatistics(url["getTankStatistics"].toString().replace("APP_ID", apiId[region].toString()), account_id, search)
    }

}