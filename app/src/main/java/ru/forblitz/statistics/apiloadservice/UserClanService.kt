package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.UserClanInformationResponse

class UserClanService(private val apiService: ApiService) : APILoadService<UserClanService.Arguments, UserClanInformationResponse>() {

    override val className: String = this.javaClass.name

    data class Arguments(val userID: String)

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getClanInfo(arguments!!.userID)
    }

    override fun parse(): UserClanInformationResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            UserClanInformationResponse::class.java)
     }

}