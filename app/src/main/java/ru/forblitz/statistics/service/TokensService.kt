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
 * The [TokensService] is responsible for getting API tokens
 */
class TokensService(
    private var connectivityService: ConnectivityService,
    private var connectivityManager: ConnectivityManager,
) {

    private var json: String? = null

    val tokens: HashMap<String, String> = HashMap()

    private var isRequestLoaded: Boolean = false

    private val taskQueue: ArrayList<Runnable> = ArrayList()

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

    private fun getLestaToken(): String {
        return ParseUtils.parseLestaAPIToken(json)
    }

    private fun getWargamingToken(): String {
        return ParseUtils.parseWargamingAPIToken(json).apply {
            tokens["eu"] = this
            tokens["na"] = this
            tokens["asia"] = this
        }
    }

    private fun getBannerAdUnitId(): String {
        return ParseUtils.parseBannerAdUnitId(json).apply { tokens["banner"] = this }
    }

    private fun getInterstitialAdUnitId(): String {
        return ParseUtils.parseInterstitialAdUnitId(json).apply { tokens["interstitial"] = this }
    }

    suspend fun updateTokens() {
        request()
        getLestaToken().apply { tokens["ru"] = this }
        getWargamingToken().apply {
            tokens["eu"] = this
            tokens["na"] = this
            tokens["asia"] = this
        }
        getBannerAdUnitId().apply { tokens["banner"] = this }
        getInterstitialAdUnitId().apply { tokens["interstitial"] = this }
        isRequestLoaded = true
        onEndOfLoad()
    }

    fun addTaskOnEndOfLoad(runnable: Runnable) {
        if (isRequestLoaded) {
            runnable.run()
        } else {
            taskQueue.add(runnable)
        }
    }

    private fun onEndOfLoad() {
        taskQueue.forEach { it.run() }
    }

}