package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.FullClanInfo
import ru.forblitz.statistics.dto.ShortClanInfo
import ru.forblitz.statistics.utils.Utils

class ClanService(private var apiService: ApiService) {

    private var fullClanInfo: FullClanInfo? = null

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

    fun getFullClanInfo(): FullClanInfo? {

        return fullClanInfo

    }

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

    fun clear() {
        fullClanInfo = null
    }

}