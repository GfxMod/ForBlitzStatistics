package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.utils.Utils

class UserClanService(private var apiService: ApiService) {

    private var shortClanInfo: ShortClanInfo? = null

    /**
     * Load [ShortClanInfo] for [userID]
     * @param userID player ID
     * @return [ShortClanInfo] for [userID]
     */
    suspend fun getShortClanInfo(userID: String): ShortClanInfo? {

        return if (shortClanInfo != null) {

            shortClanInfo

        } else {

            shortClanInfo = parse(Utils.toJson(apiService.getClanInfo(userID)), userID)

            shortClanInfo

        }

    }

    /**
     * Short form of [getShortClanInfo()][getShortClanInfo]
     * @throws [NullPointerException] if data not already loaded
     * @return [ShortClanInfo] object
     */
    fun getShortClanInfo(): ShortClanInfo {

        return shortClanInfo!!

    }

    private fun parse(json: String, userID: String): ShortClanInfo? {

        val smallJsonObject = JsonParser
            .parseString(json)
            .asJsonObject
            .getAsJsonObject("data")
            .getAsJsonObject(userID)

        if (smallJsonObject != null) {
            shortClanInfo = Gson().fromJson(
                smallJsonObject,
                ShortClanInfo::class.java
            )
        }

        return shortClanInfo

    }

    /**
     * Clears saved data
     */
    fun clear() {
        shortClanInfo = null
    }

}