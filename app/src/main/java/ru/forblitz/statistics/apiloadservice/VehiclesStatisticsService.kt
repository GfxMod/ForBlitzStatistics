package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.VehiclesStatisticsResponse

class VehiclesStatisticsService(private val apiService: ApiService) : APILoadService<VehiclesStatisticsService.Arguments, VehiclesStatisticsResponse>() {

    companion object {
    }

    override val className: String = this.javaClass.name

    data class Arguments(val userID: String, val vehiclesIDs: List<String>)

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getTankStatistics(arguments!!.userID, arguments!!.vehiclesIDs.joinToString(","))
    }

    override fun parse(): VehiclesStatisticsResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            VehiclesStatisticsResponse::class.java)
    }

}