package ru.forblitz.statistics.service

import com.google.gson.Gson
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ApiResponse
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.utils.Utils

class VehicleSpecsService(private var apiService: ApiService) {

    private val list = HashMap<String, VehicleSpecs>()

    fun getListSize(): Int {
        return list.size
    }

    /**
     * Load all vehicles specifications
     * @return [HashMap], where the key is the vehicle ID, and the contents are
     * [specifications][VehicleSpecs]
     */
    suspend fun getVehiclesSpecsList(): HashMap<String, VehicleSpecs> {

        if (list.size == 0) {

            Gson().fromJson(Utils.toJson(apiService.getAllInformationAboutVehicles()), ApiResponse::class.java).data.entries.forEach { entry: Map.Entry<String, VehicleSpecs> ->
                list[entry.key] = entry.value
            }

        }

        return list

    }

    override fun toString(): String {
        return list.toString()
    }

}