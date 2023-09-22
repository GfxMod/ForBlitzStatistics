package ru.forblitz.statistics.service

import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.Constants.PlayerStatisticsTypes
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.StatisticsDataUtils
import ru.forblitz.statistics.utils.Utils

/**
 * Responsible for obtaining all types of player statistics
 */
class UserStatisticsService(
    private var apiService: ApiService,
) : DeferredTasksService() {

    private var json: String? = null
    private var timestamp: String? = null
    private var randomStatisticsData: StatisticsData? = null
    private var ratingStatisticsData: StatisticsData? = null
    private var clanStatisticsData: StatisticsData? = null
    private var detailedAverageDamage: Boolean = false

    private suspend fun request(userID: String, detailedAverageDamage: Boolean) {
        this.detailedAverageDamage = detailedAverageDamage

        Utils.toJson(apiService.getUsers(userID)).apply {

            json = this

            timestamp = ParseUtils.parseTimestamp(this, false)

            randomStatisticsData =
                StatisticsDataUtils.parse(
                    this,
                    PlayerStatisticsTypes.RANDOM,
                    detailedAverageDamage
                )

            ratingStatisticsData =
                StatisticsDataUtils.parse(
                    this,
                    PlayerStatisticsTypes.RATING,
                    detailedAverageDamage
                )

            clanStatisticsData =
                StatisticsDataUtils.parse(
                    this,
                    PlayerStatisticsTypes.CLAN,
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
        return clanStatisticsData!!
    }

    fun getStatisticsData(playerStatisticsTypes: Collection<String>): StatisticsData {
        return StatisticsDataUtils.parse(json, playerStatisticsTypes, detailedAverageDamage)
    }

    fun clear() {
        json = null
        isRequestLoaded = false
        timestamp = null
        randomStatisticsData = null
        ratingStatisticsData = null
        clanStatisticsData = null
        detailedAverageDamage = false
    }

}