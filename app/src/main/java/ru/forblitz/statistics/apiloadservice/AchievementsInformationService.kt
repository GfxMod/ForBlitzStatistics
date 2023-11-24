package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.AchievementsInformationResponse

class AchievementsInformationService(private val apiService: ApiService) : APILoadService<Any?, AchievementsInformationResponse>() {

    override val className: String = this.javaClass.name

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getAchievementsInfo()
    }

    override fun parse(): AchievementsInformationResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            AchievementsInformationResponse::class.java)
    }

}