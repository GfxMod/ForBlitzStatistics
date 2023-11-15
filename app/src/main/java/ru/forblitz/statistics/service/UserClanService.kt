package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.utils.Utils

/**
 * The [UserClanService] class handles operations related to the user's clan, such
 * as getting the ID of the clan the user belongs to and others.
 *
 * @property shortClanInfo The last data loaded after the cleanup.
 */
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
        // TODO remove, only for Crashlytics debugging
        try {
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
        } catch (e: Exception) {
            throw Exception("userID = $userID, json = $json, message = ${e.message}")
        }
    }

    /**
     * Clears saved data
     */
    fun clear() {
        shortClanInfo = null
    }

}