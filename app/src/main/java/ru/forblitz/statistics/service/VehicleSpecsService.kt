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

    suspend fun get(): HashMap<String, VehicleSpecs> {

        if (list.size == 0) {

            Gson().fromJson(request(), ApiResponse::class.java).data.entries.forEach { entry: Map.Entry<String, VehicleSpecs> ->
                list[entry.key] = entry.value
            }

        }

        return list

    }

    // TODO: во многих сервисах нет смысла выделять request в отдельный метод,
    // но хорошая практика использовать вспомогательные переменные.
    // результатом любого запроса должна быть уже десериализованная сущность

    // сервис <== мы здесь
    // --------------------------- все, что ниже, следует воспринимать как единый этап
    // сериализатор/десериализатор
    // отправитель http-запросов
    private suspend fun request(): String {

        return Utils.toJson(apiService.getAllInformationAboutVehicles())

    }

    override fun toString(): String {
        return list.toString()
    }

}