package ru.forblitz.statistics.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.dto.UserStatisticsResponse

class StatisticsDataUtils {
    companion object {

        fun calculateFieldDifferences(
            current: StatisticsData,
            session: StatisticsData,
        ): StatisticsData {
            val result = StatisticsData()
            val fieldNames = result.javaClass.declaredFields.map { it.name }.toTypedArray()
            for (fieldName in fieldNames) {
                try {
                    val field = StatisticsData::class.java.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val currentValue = field[current] as Int
                    val sessionValue = field[session] as Int
                    field[result] = currentValue - sessionValue
                } catch (e: Exception) {
                    Log.e(StatisticsDataUtils::javaClass.name, "fieldName = $fieldName; $e")
                }
            }
            return result
        }

        fun calculateSessionDifferences(
            current: StatisticsData,
            session: StatisticsData,
        ): StatisticsData {
            return calculateFieldDifferences(current, session).apply {
                winningPercentage = current.winningPercentage!! - session.winningPercentage!!
                winningPercentage = current.winningPercentage!! - session.winningPercentage!!
                averageDamage = current.averageDamage!! - session.averageDamage!!
                efficiency = current.efficiency!! - session.efficiency!!
                survivedPercentage = current.survivedPercentage!! - session.survivedPercentage!!
                hitsOutOfShots = current.hitsOutOfShots!! - session.hitsOutOfShots!!
                averageXp = current.averageXp!! - session.averageXp!!
            }
        }

        fun parse(json: String, accountID: String, key: String): StatisticsData {
            return Gson().fromJson(
                JsonParser
                    .parseString(json)
                    .asJsonObject,
                UserStatisticsResponse::class.java)
                .data[accountID]!!
                .statistics[key]!!
        }

        fun parse(json: String, accountID: String, statisticsTypes: Collection<String>): StatisticsData {
            var result: StatisticsData? = null

            if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RANDOM)) {
                result = parse(json, accountID, Constants.PlayerStatisticsTypes.RANDOM)
            }
            if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.RATING)) {
                if (result == null) {
                    result = parse(json, accountID, Constants.PlayerStatisticsTypes.RATING)
                } else {
                    result += parse(json, accountID, Constants.PlayerStatisticsTypes.RATING)
                }

            }
            if (statisticsTypes.contains(Constants.PlayerStatisticsTypes.CLAN)) {
                if (result == null) {
                    result = parse(json, accountID, Constants.PlayerStatisticsTypes.CLAN)
                } else {
                    result += parse(json, accountID, Constants.PlayerStatisticsTypes.CLAN)
                }

            }

            return result ?: StatisticsData()
        }

    }
}