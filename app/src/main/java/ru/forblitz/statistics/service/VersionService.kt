package ru.forblitz.statistics.service

import android.app.Activity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.forblitz.statistics.api.ApiInterfaceVersion
import ru.forblitz.statistics.api.NetworkConnectionInterceptor
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

class VersionService(private var activity: Activity) {

    private var json: String? = null

    private suspend fun request() {

        json = Utils.toJson(
            Retrofit.Builder()
                .baseUrl("https://forblitz.ru/")
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(NetworkConnectionInterceptor(activity))
                        .build()
                )
                .build()
                .create(ApiInterfaceVersion::class.java)
                .getCurrent()
        )

    }

    suspend fun getCurrentAppVersion(): Int {
        if (json == null) {
            request()
        }
        return ParseUtils.currentAppVersion(json)
    }

    suspend fun getMinimalAppVersion(): Int {
        if (json == null) {
            request()
        }
        return ParseUtils.minimalAppVersion(json)
    }

}