package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.AchievementInfo
import ru.forblitz.statistics.dto.AchievementsInfoMap
import ru.forblitz.statistics.utils.Utils

class AchievementsInfoService(private var apiService: ApiService) : DeferredTasksService() {

    private var achievementsInfo: HashMap<String, AchievementInfo>? = null

    suspend fun getAchievementsInfo(): HashMap<String, AchievementInfo> {

        if (achievementsInfo == null) {
            achievementsInfo = parse(Utils.toJson(apiService.getAchievementsInfo()))
        }
        onEndOfLoad()
        return achievementsInfo!!

    }

    private fun parse(json: String): HashMap<String, AchievementInfo> {

        achievementsInfo = HashMap()
        Gson()
            .fromJson(
                JsonParser
                    .parseString(json)
                    .asJsonObject,
                AchievementsInfoMap::class.java
            )
            .achievements
            .entries
            .forEach { entry: Map.Entry<String, AchievementInfo> ->
                achievementsInfo!![entry.key] = entry.value
            }
        return achievementsInfo!!

    }


}