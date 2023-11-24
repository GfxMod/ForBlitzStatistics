package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.UserAchievementsResponse

class UserAchievementsService(private val apiService: ApiService) : APILoadService<UserAchievementsService.Arguments, UserAchievementsResponse>() {

    override val className: String = this.javaClass.name

    data class Arguments(val userID: String)

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getAchievements(arguments!!.userID)
    }

    override fun parse(): UserAchievementsResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            UserAchievementsResponse::class.java)
    }

}