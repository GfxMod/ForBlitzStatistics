package ru.forblitz.statistics.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.dto.StatisticsDataModern
import ru.forblitz.statistics.dto.UserStatisticsResponse

class StatisticsDataModernUtils {
    companion object {

        fun calculateFieldDifferences(
            current: StatisticsDataModern,
            session: StatisticsDataModern,
        ): StatisticsDataModern {
            val result = StatisticsDataModern()
            val fieldNames = result.javaClass.declaredFields.map { it.name }.toTypedArray()
            for (fieldName in fieldNames) {
                try {
                    val field = StatisticsDataModern::class.java.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val currentValue = field[current] as Int
                    val sessionValue = field[session] as Int
                    field[result] = currentValue - sessionValue
                } catch (e: Exception) {
                    Log.e(StatisticsDataModernUtils::javaClass.name, "fieldName = $fieldName; $e")
                }
            }
            return result
        }

        fun calculateSessionDifferences(
            current: StatisticsDataModern,
            session: StatisticsDataModern,
        ): StatisticsDataModern {
            val sessionDifferences = calculateFieldDifferences(current, session)
            sessionDifferences.winningPercentage = current.winningPercentage!! - session.winningPercentage!!
/*
            sessionDifferences.winRate =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.winRate.toDouble() - session.winRate.toDouble()
                )
            sessionDifferences.averageDamage =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.averageDamage.toDouble() - session.averageDamage.toDouble()
                )
            sessionDifferences.efficiency =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.efficiency.toDouble() - session.efficiency.toDouble()
                )
            sessionDifferences.survived =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.survived.toDouble() - session.survived.toDouble()
                )
            sessionDifferences.hitsFromShots =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.hitsFromShots.toDouble() - session.hitsFromShots.toDouble()
                )
            sessionDifferences.averageXp =
                String.format(
                    Locale.US,
                    "%.2f",
                    current.averageXp.toDouble() - session.averageXp.toDouble()
                )
*/
            Log.d("sessionDifferences", "declared: ${sessionDifferences.averageDamage}, maybe: ${current.averageDamage!! - session.averageDamage!!}")
            return sessionDifferences
        }

        fun parse(json: String, accountID: String, key: String): StatisticsDataModern {
            return Gson().fromJson(
                JsonParser
                    .parseString(json)
                    .asJsonObject,
                UserStatisticsResponse::class.java)
                .data[accountID]!!
                .statistics[key]!!
        }

        fun parse(json: String, accountID: String, statisticsTypes: Collection<String>): StatisticsDataModern {
            var result: StatisticsDataModern? = null

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

            return result ?: StatisticsDataModern()
        }

    }
}