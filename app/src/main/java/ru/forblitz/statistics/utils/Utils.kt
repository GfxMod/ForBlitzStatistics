package ru.forblitz.statistics.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.dto.RequestLogItem

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
    fun getStringResourceByName(context: Context, resourceName: String): String {
        return context.getString(
            context.resources
                .getIdentifier(resourceName, ResourceType.string.toString(), context.packageName)
        )
    }

    @JvmStatic
    fun getDrawableResourceByName(context: Context, resourceName: String): Drawable? {
        return AppCompatResources.getDrawable(
            context,
            context.resources
                .getIdentifier(resourceName, ResourceType.drawable.toString(), context.packageName)
        )
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

    @JvmStatic
    fun isResourceExists(context: Context, resourceName: String, resourceType: ResourceType): Boolean {
        return context.resources
            .getIdentifier(
                resourceName,
                resourceType.toString(),
                context.packageName
            ) != 0
    }

    enum class ResourceType {
        drawable,
        string
    }

}