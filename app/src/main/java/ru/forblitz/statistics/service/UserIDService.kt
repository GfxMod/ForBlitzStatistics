package ru.forblitz.statistics.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.R
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.exception.ObjectException
import ru.forblitz.statistics.dto.ErrorDTO
import ru.forblitz.statistics.utils.Utils

class UserIDService(private var context: Context, private var apiService: ApiService) {

    private var userID: String? = null
    private var objectException: ObjectException? = null


    // TODO: пусть кусок кода parse(request(...)) в случае бросает исключение

    @Throws(ObjectException::class)
    suspend fun get(nickname: String): String {

        return if (userID != null) {

            if (objectException != null) { throw objectException as ObjectException }

            userID.toString()

        } else {

            userID = parse(request(nickname))

            if (objectException != null) { throw objectException as ObjectException }

            userID as String

        }

    }

    @Throws(ObjectException::class)
    fun get(): String {

        if (objectException != null) { throw objectException as ObjectException }
        return userID!!

    }

    private suspend fun request(nickname: String): String {

        return Utils.toJson(apiService.getAccountId(nickname))

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

            objectException = ObjectException(error)

        } else if (
            JsonParser
                .parseString(json)
                .asJsonObject
                .getAsJsonObject("meta")
                .get("count")
                .asInt == 0
        ) {

            objectException =  ObjectException(
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

        }

        return userID

    }

    fun clear() {
        objectException = null
        userID = null
    }

}