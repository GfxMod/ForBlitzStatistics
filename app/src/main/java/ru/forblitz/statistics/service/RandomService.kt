package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

class RandomService(private var apiService: ApiService) {

    private var statisticsData: StatisticsData? = null
    private var json: String? = null

    fun getJson(): String {
        return json!!
    }

    // TODO: хороший нейминг - писать не просто гет, а getЧтоКонкретно
    suspend fun get(userID: String): StatisticsData {

        return if (statisticsData != null) {

            statisticsData as StatisticsData

        } else {

            statisticsData = ParseUtils.statisticsData(request(userID), "all")

            statisticsData as StatisticsData

        }

    }

    fun get(): StatisticsData? {
        return statisticsData
    }

    private suspend fun request(userID: String): String {

        json = Utils.toJson(apiService.getUsers(userID))
        return json!!

    }

    fun clear() {
        json = null
        statisticsData = null
    }

}