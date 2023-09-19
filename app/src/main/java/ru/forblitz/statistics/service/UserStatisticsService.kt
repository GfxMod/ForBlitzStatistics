package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

class UserStatisticsService(
    private var apiService: ApiService
) : DeferredTasksService() {

    private var json: String? = null
    private var timestamp: String? = null
    private var randomStatisticsData: StatisticsData? = null
    private var ratingStatisticsData: StatisticsData? = null
    private var clanStatisticsData: StatisticsData? = null

    private suspend fun request(userID: String, detailedAverageDamage: Boolean) {

        Utils.toJson(apiService.getUsers(userID)).apply {

            json = this

            timestamp = ParseUtils.parseTimestamp(this, false)

            randomStatisticsData = ParseUtils.parseStatisticsData(
                this,
                Constants.StatisticKeys.random,
                detailedAverageDamage
            )

            ratingStatisticsData = ParseUtils.parseStatisticsData(
                this,
                Constants.StatisticKeys.rating,
                detailedAverageDamage
            )

            clanStatisticsData = ParseUtils.parseStatisticsData(
                this,
                Constants.StatisticKeys.clan,
                detailedAverageDamage
            )

        }

    }

    suspend fun loadStatistics(userID: String, detailedAverageDamage: Boolean) {
        request(userID, detailedAverageDamage)
        onEndOfLoad()
    }

    fun getJson(): String {
        return json!!
    }

    fun getTimestamp(): String {
        return timestamp!!
    }

    fun getRandomStatisticsData(): StatisticsData {
        return randomStatisticsData!!
    }

    fun getRatingStatisticsData(): StatisticsData {
        return ratingStatisticsData!!
    }

    fun getClanStatisticsData(): StatisticsData {
        return randomStatisticsData!!
    }

    fun clear() {
        json = null
        isRequestLoaded = false
        timestamp = null
        randomStatisticsData = null
        ratingStatisticsData = null
        clanStatisticsData = null
    }

}