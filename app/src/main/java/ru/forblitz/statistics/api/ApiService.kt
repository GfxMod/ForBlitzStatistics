package ru.forblitz.statistics.api

import android.app.Activity
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class ApiService(private val activity: Activity) {

    lateinit var region: String

    suspend fun getAccountId(search: String): Response<ResponseBody> {
    
        // TODO: в when или switch, можно сразу несколько значений указать через запятую
        // "eu", "na" -> ...

        // Этот кусок кода с выбором API-интерфейса очень сильно конфузит.
        // Надо спрятать его в отдельный метод!

        // Аналогично - Lesta и WG - интерфейсы ничем не отличаются, кроме API-ключа
        // Их надо вообще объединить и сделать отдельный класс, который будет возвращать подходящий клиент
        // по региону. И автоматически подставит из настроек нужный ключ и url

        // Если кратко:
        // 1. Сделать ApiInterfaceLesta и ApiInterfaceWG полиморфными друг другу (короче, оставить один, и добавить в него конструктор по url и ключу)
        // 2. Убрать этот странный выбор региона из всех методов, содержащихся здесь - перенести эту функцию в getClient()

        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getAccountId(search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getAccountId(search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getAccountId(search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getAccountId(search)
            }
        }
    }

    suspend fun getUsers(search: String): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getUsers(search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getUsers(search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getUsers(search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getUsers(search)
            }
        }
    }

    suspend fun getClanInfo(search: String): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getClanInfo(search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getClanInfo(search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getClanInfo(search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getClanInfo(search)
            }
        }
    }

    suspend fun getFullClanInfo(search: String): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getFullClanInfo(search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getFullClanInfo(search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getFullClanInfo(search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getFullClanInfo(search)
            }
        }
    }

    suspend fun getAchievements(search: String): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getAchievements(search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getAchievements(search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getAchievements(search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getAchievements(search)
            }
        }
    }

    suspend fun getAllInformationAboutVehicles(): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getAllInformationAboutVehicles()
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getAllInformationAboutVehicles()
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getAllInformationAboutVehicles()
            }
            else -> {
                (getClient() as ApiInterfaceWG).getAllInformationAboutVehicles()
            }
        }
    }

    suspend fun getTankStatistics(account_id: String, search: String): Response<ResponseBody> {
        return when (region) {
            "ru" -> {
                (getClient() as ApiInterfaceLesta).getTankStatistics(account_id, search)
            }
            "eu" -> {
                (getClient() as ApiInterfaceWG).getTankStatistics(account_id, search)
            }
            "na" -> {
                (getClient() as ApiInterfaceWG).getTankStatistics(account_id, search)
            }
            else -> {
                (getClient() as ApiInterfaceWG).getTankStatistics(account_id, search)
            }
        }
    }

    private fun getClient(): Any {
        return when (region) {
            "ru" -> {
                Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceLesta::class.java)
            }
            "eu" -> {
                Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.eu/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
            }
            "na" -> {
                Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.com/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
            }
            else -> {
                Retrofit.Builder()
                    .baseUrl("https://api.wotblitz.asia/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(activity)
                    ).build())
                    .build().create(ApiInterfaceWG::class.java)
            }
        }
    }

}