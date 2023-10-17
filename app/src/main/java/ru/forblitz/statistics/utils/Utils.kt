package ru.forblitz.statistics.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.dto.RequestLogItem
import kotlin.collections.ArrayList

object Utils {

    @JvmStatic
    fun toJson(response: Response<ResponseBody>): String {
        return GsonBuilder().setPrettyPrinting().create().toJson(
            JsonParser.parseString(
                response.body()
                    ?.string()
            )
        )
    }

    @JvmStatic
    fun getStringResourceByName(context: Context, stringName: String): String {
        val packageName: String = context.packageName
        val resId: Int = context.resources.getIdentifier(stringName, "string", packageName)
        return context.getString(resId)
    }

    @JvmStatic
    fun getWithoutEndedRecords(records: ArrayList<RequestLogItem>): ArrayList<RequestLogItem> {
        return ArrayList<RequestLogItem>().apply {
            records.forEach {
                if (!it.isCompleted) {
                    this.add(it)
                }
            }
        }
    }

}