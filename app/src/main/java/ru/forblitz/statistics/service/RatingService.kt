package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

class RatingService(private var apiService: ApiService) {

    private var statisticsData: StatisticsData? = null

    suspend fun getStatisticsData(userID: String): StatisticsData {

        return if (statisticsData != null) {

            statisticsData as StatisticsData

        } else {

            val json = request(userID)

            statisticsData = ParseUtils.statisticsData(json, "rating")

            statisticsData as StatisticsData

        }

    }

    fun getStatisticsData(): StatisticsData? {
        return statisticsData
    }

    private suspend fun request(userID: String): String {

        return Utils.toJson(apiService.getUsers(userID))

    }

    fun clear() {
        statisticsData = null
    }

}