package ru.forblitz.statistics.service

import com.google.gson.Gson
import com.google.gson.JsonParser
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.VehicleStat
import ru.forblitz.statistics.utils.Utils
import java.util.*
import kotlin.math.ceil

/**
 * The [VehicleStatService] class handles operations related to getting a list
 * of vehicles [statistics][VehicleStat].
 *
 * @property list [HashMap], where the key is the vehicle ID, and the content
 * is the [statistics of the vehicle][VehicleStat]
 */
class VehicleStatService(private var apiService: ApiService) {

    private var list = HashMap<String, VehicleStat>()

    /**
     * Load statistics for [vehiclesIds]
     * @return [HashMap], where the key is the vehicle ID, and the content is
     * the [statistics of the vehicle][VehicleStat]
     */
    suspend fun getVehicleStat(userID: String, vehiclesIds: Array<String>): HashMap<String, VehicleStat> {

        if (list.size == 0) {
            val separatedIds = splitIntoBatches(vehiclesIds)

            for (i in 0 until separatedIds.size) {

                val jsonArray = JsonParser
                    .parseString(request(userID, separatedIds[i]))
                    .asJsonObject
                    .getAsJsonObject("data")
                    .getAsJsonArray(userID)

                if (jsonArray != null) {
                    val vehicleStats: Array<VehicleStat> = Gson().fromJson(
                        jsonArray,
                        Array<VehicleStat>::class.java
                    )

                    for (vehicleStatDTO in vehicleStats) {
                        list[vehicleStatDTO.tankId] = vehicleStatDTO.apply { vehicleStatDTO.all.calculate() }
                    }
                }

            }
        }

        return list

    }

    private suspend fun request(userID: String, vehiclesIds: String): String {

        return Utils.toJson(apiService.getTankStatistics(userID, vehiclesIds))

    }

    private fun splitIntoBatches(vehiclesIds: Array<String>): ArrayList<String> {
        val separatedIds = ArrayList<String>()

        // According to the WoT/Tanks Blitz API documentation, the maximum number of IDs is 100.
        val countOfRequests = ceil((vehiclesIds.size - 1).toDouble() / 100).toInt()

        for (i in 0 until countOfRequests) {
            val listOfIdsForRequest: StringBuilder = StringBuilder()

            val countOfIdsInCurrentRequest = if (vehiclesIds.size - i * 100 >= 100) { 100 } else { vehiclesIds.size - i * 100 }
            for (j in 0 until countOfIdsInCurrentRequest) {
                listOfIdsForRequest.append(vehiclesIds[i * 100 + j]).append(",")
            }

            listOfIdsForRequest.deleteCharAt(listOfIdsForRequest.lastIndexOf(","))
            separatedIds.add(listOfIdsForRequest.toString())
        }

        return separatedIds
    }

    fun clear() {
        list = HashMap()
    }

    override fun toString(): String {
        return list.toString()
    }

}