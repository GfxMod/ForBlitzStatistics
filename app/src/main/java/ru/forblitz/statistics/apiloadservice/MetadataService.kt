package ru.forblitz.statistics.apiloadservice

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.api.ApiServiceForBlitz
import ru.forblitz.statistics.dto.MetadataResponse

class MetadataService(private val apiServiceForBlitz: ApiServiceForBlitz) : APILoadService<Any?, MetadataResponse>() {

    override val className: String = this.javaClass.name

    val tokens: HashMap<String, String> = HashMap()
        get() {
            return field.apply {
                data?.let { metadataResponse ->
                    metadataResponse.statisticsApiKeys?.let {
                        this["ru"] = it.lesta
                        this["eu"] = it.wargaming
                        this["na"] = it.wargaming
                        this["asia"] = it.wargaming
                    }
                    metadataResponse.statisticsAdUnitIds?.let {
                        this["banner"] = it.banner
                        this["interstitial"] = it.interstitial
                    }
                }
            }
        }

    val minimalAppVersion: Int
        get() = data!!.statisticAppVersion!!.minimalAppVersion

    val currentAppVersion: Int
        get() = data!!.statisticAppVersion!!.currentAppVersion

    override suspend fun request(): Response<ResponseBody> {
        return apiServiceForBlitz.getAll()
    }

    override fun parse(): MetadataResponse {
        return Gson().fromJson(
            JsonParser
                .parseString(json)
                .asJsonObject,
            MetadataResponse::class.java)
    }

    override fun onEndOfLoad() {
        super.onEndOfLoad()
        // call getter of tokens for initializing changes
        tokens
    }

}
