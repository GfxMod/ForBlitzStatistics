package ru.forblitz.statistics.api

import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import ru.forblitz.statistics.data.Constants.baseUrl
import ru.forblitz.statistics.data.Constants.okHttpTimeout
import ru.forblitz.statistics.data.Constants.url
import ru.forblitz.statistics.dto.RequestLogItem
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.RequestLogService
import ru.forblitz.statistics.dto.RequestLogItem.RequestType
import java.time.Duration

class ApiService(
    private val connectivityService: ConnectivityService,
    private val connectivityManager: ConnectivityManager,
    private val requestLogService: RequestLogService,
    private val tokens: HashMap<String, String>
) {

    private lateinit var region: String
    private lateinit var apiInterface: ApiInterface

    fun setRegion(region: String): ApiInterface {
        this.region = region
        apiInterface = Retrofit.Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(NetworkConnectionInterceptor(connectivityService, connectivityManager))
                    .connectTimeout(Duration.ofSeconds(okHttpTimeout))
                    .readTimeout(Duration.ofSeconds(okHttpTimeout))
                    .build()
            )
            .baseUrl(baseUrl[region].toString())
            .build().create(ApiInterface::class.java)
        return apiInterface
    }

    suspend fun getAccountId(search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.ACCOUNT_ID, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getAccountId(url["getAccountId"].toString().replace("APP_ID", tokens[region].toString()), search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getUsers(search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.USER_STATISTICS, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getUsers(url["getUsers"].toString().replace("APP_ID", tokens[region].toString()), search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getClanInfo(search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.USER_CLAN_INFO, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getClanInfo(url["getClanInfo"].toString().replace("APP_ID", tokens[region].toString()), search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getFullClanInfo(search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.FULL_CLAN_INFO, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getFullClanInfo(url["getFullClanInfo"].toString().replace("APP_ID", tokens[region].toString()), search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getAchievements(search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.ACHIEVEMENTS, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getAchievements(url["getAchievements"].toString().replace("APP_ID", tokens[region].toString()), search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getAllInformationAboutVehicles(): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.TANKOPEDIA, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getAllInformationAboutVehicles(url["getAllInformationAboutVehicles"].toString().replace("APP_ID", tokens[region].toString()))
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

    suspend fun getTankStatistics(account_id: String, search: String): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestType.TANKS_STATISTICS, false)
        requestLogService.addRecord(requestLogItem)
        val response = apiInterface.getTankStatistics(url["getTankStatistics"].toString().replace("APP_ID", tokens[region].toString()), account_id, search)
        requestLogService.addRecord(requestLogItem.apply { isCompleted = true })
        return response
    }

}