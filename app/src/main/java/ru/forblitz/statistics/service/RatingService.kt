package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.Utils

/**
 * The [RatingService] class handles rating-related operations.
 *
 * @property statisticsData The last data loaded after the cleanup.
 */
class RatingService(private var apiService: ApiService) {

    private var statisticsData: StatisticsData? = null

    /**
     * Load StatisticsData for [userID]
     * @param userID player ID
     * @param detailedAverageDamage need to round the average damage value to hundredths instead of integers
     * @return [StatisticsData] for [userID]
     */
    suspend fun getStatisticsData(userID: String, detailedAverageDamage: Boolean): StatisticsData {

        return if (statisticsData != null) {

            statisticsData as StatisticsData

        } else {

            val json = request(userID)

            statisticsData = ParseUtils.parseStatisticsData(json, "rating", detailedAverageDamage)

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

        return Utils.toJson(apiService.getUsers(userID))

    }

    /**
     * Clears saved data
     */
    fun clear() {
        statisticsData = null
    }

}