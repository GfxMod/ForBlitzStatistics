package ru.forblitz.statistics.utils

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.util.*

object Utils {

    @JvmStatic
    fun getProperties(name: String?, context: Context): Properties? {
        val resources = context.resources
        val assetManager = resources.assets
        return try {
            val inputStream = assetManager.open(name!!)
            val properties = Properties()
            properties.load(inputStream)
            properties
        } catch (e: IOException) {
            Log.i("AdUnitIds", "no adUnitIds.properties available")
            null
        }
    }

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

}