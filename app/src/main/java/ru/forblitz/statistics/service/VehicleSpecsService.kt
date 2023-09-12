package ru.forblitz.statistics.service

import com.google.gson.Gson
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.dto.ApiResponse
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.utils.Utils

/**
 * The [VehicleSpecsService] class handles operations related to getting a list
 * of vehicles in the game and their [characteristics][VehicleSpecs].
 *
 * @property list [HashMap], where the key is the vehicle ID, and the content
 * is the [characteristics of the vehicle][VehicleSpecs]
 */
class VehicleSpecsService(private var apiService: ApiService) {

    private val list = HashMap<String, VehicleSpecs>()

    private var isRequestLoaded: Boolean = false

    private val taskQueue: ArrayList<Runnable> = ArrayList()

    fun getListSize(): Int {
        return list.size
    }

    /**
     * Load all vehicles specifications
     * @return [HashMap], where the key is the vehicle ID, and the content is
     * the [characteristics of the vehicle][VehicleSpecs]
     */
    suspend fun getVehiclesSpecsList(): HashMap<String, VehicleSpecs> {

        if (list.size == 0) {

            Gson().fromJson(Utils.toJson(apiService.getAllInformationAboutVehicles()), ApiResponse::class.java).data.entries.forEach { entry: Map.Entry<String, VehicleSpecs> ->
                list[entry.key] = entry.value
            }

            onEndOfLoad()
        }

        return list

    }

    fun addTaskOnEndOfLoad(runnable: Runnable) {
        if (isRequestLoaded) {
            runnable.run()
        } else {
            taskQueue.add(runnable)
        }
    }

    private fun onEndOfLoad() {
        isRequestLoaded = true
        taskQueue.forEach { it.run() }
    }

    override fun toString(): String {
        return list.toString()
    }

}