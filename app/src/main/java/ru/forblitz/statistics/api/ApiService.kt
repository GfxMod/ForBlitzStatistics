package ru.forblitz.statistics.api

import android.app.Activity
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class ApiService(private val activity: Activity) {

    lateinit var region: String

    suspend fun getAccountId(search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getAccountId(search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAccountId(search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAccountId(search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAccountId(search)
            }
        }
    }

    suspend fun getUsers(search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getUsers(search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getUsers(search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getUsers(search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getUsers(search)
            }
        }
    }

    suspend fun getClanInfo(search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getClanInfo(search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getClanInfo(search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getClanInfo(search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getClanInfo(search)
            }
        }
    }

    suspend fun getFullClanInfo(search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getFullClanInfo(search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getFullClanInfo(search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getFullClanInfo(search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getFullClanInfo(search)
            }
        }
    }

    suspend fun getAchievements(search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getAchievements(search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAchievements(search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAchievements(search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAchievements(search)
            }
        }
    }

    suspend fun getAllInformationAboutVehicles(): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getAllInformationAboutVehicles()
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAllInformationAboutVehicles()
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAllInformationAboutVehicles()
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getAllInformationAboutVehicles()
            }
        }
    }

    suspend fun getTankStatistics(account_id: String, search: String): Response<ResponseBody> {
        when (region) {
            "ru" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
                    .getTankStatistics(account_id, search)
            }
            "eu" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getTankStatistics(account_id, search)
            }
            "na" -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getTankStatistics(account_id, search)
            }
            else -> {
                return Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
                    .getTankStatistics(account_id, search)
            }
        }
    }

}