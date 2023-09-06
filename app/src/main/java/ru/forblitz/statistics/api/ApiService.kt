package ru.forblitz.statistics.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import ru.forblitz.statistics.data.Constants.*
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.RequestLogService
import ru.forblitz.statistics.service.RequestLogService.RequestType

class ApiService(
    private val connectivityService: ConnectivityService,
    private val requestLogService: RequestLogService
    ) {

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
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.ACCOUNT_ID, false)
        val response = apiInterface.getAccountId(url["getAccountId"].toString().replace("APP_ID", apiId[region].toString()), search)
        requestLogService.addRecord(timestampOfSent, RequestType.ACCOUNT_ID, true)
        return response
    }

    suspend fun getUsers(search: String): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.ACCOUNT_ID, false)
        val response = apiInterface.getUsers(url["getUsers"].toString().replace("APP_ID", apiId[region].toString()), search)
        requestLogService.addRecord(timestampOfSent, RequestType.USER_STATISTICS, true)
        return response
    }

    suspend fun getClanInfo(search: String): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.USER_CLAN_INFO, false)
        val response = apiInterface.getClanInfo(url["getClanInfo"].toString().replace("APP_ID", apiId[region].toString()), search)
        requestLogService.addRecord(timestampOfSent, RequestType.USER_CLAN_INFO, true)
        return response
    }

    suspend fun getFullClanInfo(search: String): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.FULL_CLAN_INFO, false)
        val response = apiInterface.getFullClanInfo(url["getFullClanInfo"].toString().replace("APP_ID", apiId[region].toString()), search)
        requestLogService.addRecord(timestampOfSent, RequestType.FULL_CLAN_INFO, true)
        return response
    }

    suspend fun getAchievements(search: String): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.ACHIEVEMENTS, false)
        val response = apiInterface.getAchievements(url["getAchievements"].toString().replace("APP_ID", apiId[region].toString()), search)
        requestLogService.addRecord(timestampOfSent, RequestType.ACHIEVEMENTS, true)
        return response
    }

    suspend fun getAllInformationAboutVehicles(): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.TANKOPEDIA, false)
        val response = apiInterface.getAllInformationAboutVehicles(url["getAllInformationAboutVehicles"].toString().replace("APP_ID", apiId[region].toString()))
        requestLogService.addRecord(timestampOfSent, RequestType.TANKOPEDIA, true)
        return response
    }

    suspend fun getTankStatistics(account_id: String, search: String): Response<ResponseBody> {
        val timestampOfSent = System.currentTimeMillis()
        requestLogService.addRecord(timestampOfSent, RequestType.TANKS_STATISTICS, false)
        val response = apiInterface.getTankStatistics(url["getTankStatistics"].toString().replace("APP_ID", apiId[region].toString()), account_id, search)
        requestLogService.addRecord(timestampOfSent, RequestType.TANKS_STATISTICS, true)
        return response
    }

}