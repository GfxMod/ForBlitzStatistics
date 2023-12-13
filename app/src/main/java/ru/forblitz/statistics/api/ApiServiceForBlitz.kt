package ru.forblitz.statistics.api

import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.RequestLogItem
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.RequestsService

class ApiServiceForBlitz(
    private val connectivityService: ConnectivityService,
    connectivityManager: ConnectivityManager,
    private val requestsService: RequestsService
) {

    private val apiInterfaceForBlitz: ApiInterfaceForBlitz = Retrofit.Builder()
        .baseUrl(Constants.forBlitzBaseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(
                    connectivityService,
                    connectivityManager
                ))
                .build()
        )
        .build()
        .create(ApiInterfaceForBlitz::class.java)

    suspend fun getAll(): Response<ResponseBody> {
        val requestLogItem = RequestLogItem(System.currentTimeMillis(), RequestLogItem.RequestType.METADATA, false)
        requestsService.addRecord(requestLogItem)
        var response = apiInterfaceForBlitz.getAll()
        if (!response.isSuccessful || response.body() == null) {
            connectivityService.responsibleActivity?.let { connectivityService.showUkAlertDialog(it) }
            while (!response.isSuccessful || response.body() == null) {
                response = apiInterfaceForBlitz.getAll()
            }
        }
        connectivityService.responsibleActivity?.let { connectivityService.killUkAlertDialog(it) }
        requestsService.completeRecord(requestLogItem)
        return response
    }

}