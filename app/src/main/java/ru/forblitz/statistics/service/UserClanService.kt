package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.SmallClanData
import ru.forblitz.statistics.utils.Utils

class UserClanService(private var apiService: ApiService) {

    private var smallClanData: SmallClanData? = null

    suspend fun get(userID: String): SmallClanData? {

        return if (smallClanData != null) {

            smallClanData

        } else {

            smallClanData = parse(request(userID), userID)

            smallClanData

        }

    }

    fun get(): SmallClanData {

        return smallClanData!!

    }

    private suspend fun request(userID: String): String {

        return Utils.toJson(apiService.getClanInfo(userID))

    }

    private fun parse(json: String, userID: String): SmallClanData? {

        val smallJsonObject = JsonParser
            .parseString(json)
            .asJsonObject
            .getAsJsonObject("data")
            .getAsJsonObject(userID)

        if (smallJsonObject != null) {
            smallClanData = Gson().fromJson(
                smallJsonObject,
                SmallClanData::class.java
            )
        }

        return smallClanData

    }

    fun clear() {
        smallClanData = null
    }

}