package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.AccountSearchResponse

class UserService(private val apiService: ApiService) : APILoadService<UserService.Arguments, AccountSearchResponse>() {

    override val className: String = this.javaClass.name

    data class Arguments(val nickname: String)

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getAccountId(arguments!!.nickname)
    }

    override fun parse(): AccountSearchResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            AccountSearchResponse::class.java)
    }

    val nickname: String?
        get() = data?.data?.getOrNull(0)?.nickname

    val accountId: String?
        get() = data?.data?.getOrNull(0)?.accountId

}