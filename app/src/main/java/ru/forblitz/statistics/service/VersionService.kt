package ru.forblitz.statistics.service

import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.forblitz.statistics.api.ApiInterfaceForBlitz
import ru.forblitz.statistics.api.NetworkConnectionInterceptor
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

/**
 * The [VersionService] class handles operations related to obtaining the
 * minimum and recommended versions of the application
 */
class VersionService(
    private var connectivityService: ConnectivityService,
    private var connectivityManager: ConnectivityManager
) {

    private var json: String? = null

    private suspend fun request() {

        json = Utils.toJson(
            Retrofit.Builder()
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
                .getAll()
        )

    }

    suspend fun getCurrentAppVersion(): Int {
        if (json == null) {
            request()
        }
        return ParseUtils.parseCurrentAppVersion(json)
    }

    suspend fun getMinimalAppVersion(): Int {
        if (json == null) {
            request()
        }
        return ParseUtils.parseMinimalAppVersion(json)
    }

}