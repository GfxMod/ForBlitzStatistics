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
class AdUnitIdsService(
    private val connectivityService: ConnectivityService,
    private val connectivityManager: ConnectivityManager,
) {

    private var json: String? = null

    val adUnitIds: HashMap<String, String> = HashMap()

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

    suspend fun getBannerAdUnitId(): String {
        if (json == null) {
            request()
        }
        return ParseUtils.parseBannerAdUnitId(json).apply { adUnitIds["banner"] = this }
    }

    suspend fun getInterstitialAdUnitId(): String {
        if (json == null) {
            request()
        }
        return ParseUtils.parseInterstitialAdUnitId(json).apply { adUnitIds["interstitial"] = this }
    }

}