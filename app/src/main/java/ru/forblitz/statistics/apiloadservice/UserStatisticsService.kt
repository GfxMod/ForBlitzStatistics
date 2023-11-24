package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.StatisticsDataModern
import ru.forblitz.statistics.dto.UserStatisticsResponse

class UserStatisticsService(private val apiService: ApiService) : APILoadService<UserStatisticsService.Arguments, UserStatisticsResponse>() {

    override val className: String = this.javaClass.name

    data class Arguments(val userID: String)

    val randomStatistics: StatisticsDataModern
        get() = data!!.data!![arguments!!.userID]!!.statistics?.get("all")!!

    val ratingStatistics: StatisticsDataModern
        get() = data!!.data!![arguments!!.userID]!!.statistics?.get("rating")!!

    val clanStatistics: StatisticsDataModern
        get() = data!!.data!![arguments!!.userID]!!.statistics?.get("clan")!!

    val timestamp: String
        get() = data!!.data!![arguments!!.userID]!!.lastBattleTime

    fun getStatisticsByEnum(statisticsTypes: Collection<String>): StatisticsDataModern {
        var result: StatisticsDataModern? = null

        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RANDOM)) {
            result = randomStatistics
        }
        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RATING)) {
            if (result == null) {
                result = ratingStatistics
            } else {
                result += ratingStatistics
            }

        }
        if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.CLAN)) {
            if (result == null) {
                result = clanStatistics
            } else {
                result += clanStatistics
            }

        }

        return result ?: StatisticsDataModern()
    }

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getUsers(arguments!!.userID)
    }

    override fun parse(): UserStatisticsResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            UserStatisticsResponse::class.java)
    }

}