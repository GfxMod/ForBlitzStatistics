package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.dto.VehicleSpecsResponse

class VehicleSpecsService(private val apiService: ApiService) : APILoadService<Any?, VehicleSpecsResponse>() {

    override val className: String = this.javaClass.name

    val map: Map<String, VehicleSpecs>?
        get() = data?.data

    override suspend fun request(): Response<ResponseBody> {
        return apiService.getAllInformationAboutVehicles()
    }

    override fun parse(): VehicleSpecsResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            VehicleSpecsResponse::class.java)
    }

}