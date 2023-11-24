package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ClanInformationResponse

class ClanInformationService(private val apiService: ApiService) : APILoadService<ClanInformationService.Arguments, ClanInformationResponse>() {

    override val className: String = this.javaClass.name

    data class Arguments(val clanID: String)

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getFullClanInfo(arguments!!.clanID)
    }

    override fun parse(): ClanInformationResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            ClanInformationResponse::class.java)
    }

}
