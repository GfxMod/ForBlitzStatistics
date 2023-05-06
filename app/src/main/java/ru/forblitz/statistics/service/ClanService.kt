package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.BigClanData
import ru.forblitz.statistics.dto.SmallClanData
import ru.forblitz.statistics.utils.Utils

class ClanService(private var apiService: ApiService) {

    private var bigClanData: BigClanData? = null

    suspend fun get(smallClanData: SmallClanData): BigClanData? {

        return if (bigClanData != null) {

            bigClanData as BigClanData

        } else if (smallClanData.clanId == null) {

            null

        } else {

            bigClanData = parse(request(smallClanData.clanId), smallClanData.clanId)

            bigClanData as BigClanData

        }

    }

    fun get(): BigClanData? {

        return bigClanData

    }

    private suspend fun request(clanID: String): String {

        return Utils.toJson(apiService.getFullClanInfo(clanID))

    }

    private fun parse(json: String, clanID: String): BigClanData? {

        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonObject("data")
                .getAsJsonObject(clanID),
            BigClanData::class.java
        )

    }

    fun clear() {
        bigClanData = null
    }

}