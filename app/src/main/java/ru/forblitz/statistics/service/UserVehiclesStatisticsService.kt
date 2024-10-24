package ru.forblitz.statistics.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.apiloadservice.VehiclesStatisticsService
import ru.forblitz.statistics.dto.VehiclesStatisticsResponse.VehicleStatistics
import java.lang.NullPointerException
import java.util.concurrent.CopyOnWriteArrayList

class UserVehiclesStatisticsService(private val apiService: ApiService) {

    companion object {
        const val MAX_VEHICLES: Int = 100
    }

    var data: Map<String, VehicleStatistics>? = null
        private set

    suspend fun get(userID: String, vehiclesIDs: Collection<String>): Map<String, VehicleStatistics> {
        if (data == null) {
            with(CopyOnWriteArrayList<VehicleStatistics>()) {
                val jobs: ArrayList<Job> = ArrayList()
                vehiclesIDs.chunked(MAX_VEHICLES)
                    .forEach { chunkedVehiclesIDs ->
                        jobs.add(CoroutineScope(Dispatchers.IO).launch {
                            this@with.addAll(
                                try {
                                    VehiclesStatisticsService(apiService)
                                        .get(
                                            VehiclesStatisticsService.Arguments(
                                                userID,
                                                chunkedVehiclesIDs
                                            )
                                        )
                                        .data!![userID]!!
                                } catch (e: NullPointerException) {
                                    arrayOf()
                                }
                            )
                        })
                    }
                jobs.joinAll()
                data = this@with.associateBy( { it.tankId }, { it } )
            }
        }
        return data!!
    }

    fun clear() {
        data = null
    }

}