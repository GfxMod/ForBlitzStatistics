package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.FullClanInfo
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.utils.Utils

/**
 * The [ClanService] class handles clan-related operations.
 *
 * @property fullClanInfo The.
 */
class ClanService(private var apiService: ApiService) {

    private var fullClanInfo: FullClanInfo? = null

    /**
     * Load full clan info for [shortClanInfo.clanId][ShortClanInfo.clanId]
     * if it not exists
     *
     * @param shortClanInfo shortClanInfo, from where the clan ID will be taken if necessary
     * @return [FullClanInfo] for [shortClanInfo.clanId][ShortClanInfo.clanId]
     */
    suspend fun getFullClanInfo(shortClanInfo: ShortClanInfo): FullClanInfo? {

        return if (fullClanInfo != null) {

            fullClanInfo as FullClanInfo

        } else if (shortClanInfo.clanId == null) {

            null

        } else {

            fullClanInfo = parse(Utils.toJson(apiService.getFullClanInfo(shortClanInfo.clanId)), shortClanInfo.clanId)

            fullClanInfo as FullClanInfo

        }

    }

    /**
     * Short form of [getFullClanInfo()][getFullClanInfo]
     * @return [FullClanInfo] object if it exists, null if not
     */
    fun getFullClanInfo(): FullClanInfo? {

        return fullClanInfo

    }

    /**
     * Serializes [json] to [FullClanInfo]
     *
     * @param json json to serialize
     * @param clanID clan ID
     * @return Serialized [FullClanInfo] object
     */
    private fun parse(json: String, clanID: String): FullClanInfo? {

        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonObject("data")
                .getAsJsonObject(clanID),
            FullClanInfo::class.java
        )

    }

    /**
     * Clears saved data
     */
    fun clear() {
        fullClanInfo = null
    }

}