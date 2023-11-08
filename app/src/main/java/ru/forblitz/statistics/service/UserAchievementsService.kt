package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.UserAchievementsMap
import ru.forblitz.statistics.utils.Utils

class UserAchievementsService(private var apiService: ApiService) {

    private var userAchievements: HashMap<String, Int>? = null

    suspend fun getUserAchievements(userID: String): HashMap<String, Int> {

        if (userAchievements == null) {
            userAchievements = parse(Utils.toJson(apiService.getAchievements(userID)), userID)
        }
        return userAchievements!!

    }

    private fun parse(json: String, userID: String): HashMap<String, Int> {

        userAchievements = HashMap()
        Gson()
            .fromJson(
                JsonParser
                    .parseString(json)
                    .asJsonObject
                    .getAsJsonObject("data")
                    .getAsJsonObject(userID),
                UserAchievementsMap::class.java
            )
            .achievements
            .entries
            .forEach { entry: Map.Entry<String, Int> ->
                userAchievements!![entry.key] = entry.value
            }
        return userAchievements!!

    }

    fun clear() {
        userAchievements = null
    }

}