package ru.forblitz.statistics.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.R
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ErrorDTO
import ru.forblitz.statistics.exception.ObjectException
import ru.forblitz.statistics.utils.Utils

class UserService(private var context: Context, private var apiService: ApiService) {

    private var userID: String? = null
    private var nickname: String? = null

    /**
     * Load userID for [nickname]
     * @param nickname player nickname
     * @return [userID] for [nickname]
     */
    @Throws(ObjectException::class)
    suspend fun getUserID(nickname: String): String {
        return if (userID != null) {

            userID.toString()

        } else {

            userID = parse(Utils.toJson(apiService.getAccountId(nickname)))

            userID as String

        }
    }

    fun getUserID(): String {
        return userID!!
    }

    fun getNickname(): String {
        return nickname!!
    }

    private fun parse(json: String): String? {
        if (
            JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonObject("error") != null
        ) {

            val error = Gson().fromJson(
                JsonParser.parseString(json).asJsonObject.getAsJsonObject("error"),
                ErrorDTO::class.java
            )

            throw ObjectException(error)

        } else if (
            JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonObject("meta")
                .get("count")
                .asInt == 0
        ) {

            throw ObjectException(
                ErrorDTO(
                    context.getString(R.string.nickname_not_found),
                    "404"
                )
            )

        } else {

            userID = JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonArray("data")
                .get(0)
                .asJsonObject
                .get("account_id")
                .asString

            nickname = JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonArray("data")
                .get(0)
                .asJsonObject
                .get("nickname")
                .asString

        }

        return userID
    }

    /**
     * Clears saved data
     */
    fun clear() {
        userID = null
        nickname = null
    }

}