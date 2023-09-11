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

    suspend fun getLestaToken(): String {
        if (json == null) {
            request()
        }
/*
        val token = ParseUtils.parseLestaAPIToken(json)
        tokens["ru"] = token
        return token;
*/
        return ParseUtils.parseLestaAPIToken(json).apply { tokens["ru"] = this }
    }

    suspend fun getWargamingToken(): String {
        if (json == null) {
            request()
        }
        return ParseUtils.parseWargamingAPIToken(json).apply {
            tokens["eu"] = this
            tokens["na"] = this
            tokens["asia"] = this
        }
    }

}