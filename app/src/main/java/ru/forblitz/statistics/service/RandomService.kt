package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

/**
 * The [RandomService] class handles random-related operations.
 *
 * @property statisticsData The last data loaded after the cleanup.
 * @property json The last JSON loaded after the cleanup.
 */
class RandomService(private var apiService: ApiService) {

    private var statisticsData: StatisticsData? = null
    private var json: String? = null

    /**
     * @return Last uploaded after clearing json
     */
    fun getJson(): String {
        return json!!
    }

    /**
     * Load StatisticsData for [userID]
     * @param userID player ID
     * @return [StatisticsData] for [userID]
     */
    suspend fun getStatisticsData(userID: String): StatisticsData {

        return if (statisticsData != null) {

            statisticsData as StatisticsData

        } else {

            statisticsData = ParseUtils.statisticsData(request(userID), "all")

            statisticsData as StatisticsData

        }

    }

    /**
     * Short form of [getStatisticsData()][getStatisticsData]
     * @return [StatisticsData] object if it exists, null if not
     */
    fun getStatisticsData(): StatisticsData? {
        return statisticsData
    }

    private suspend fun request(userID: String): String {

        json = Utils.toJson(apiService.getUsers(userID))
        return json!!

    }

    /**
     * Clears saved data
     */
    fun clear() {
        json = null
        statisticsData = null
    }

}