package ru.forblitz.statistics.apiloadservice

import okhttp3.ResponseBody
import retrofit2.Response
import ru.forblitz.statistics.dto.APIResponse
import ru.forblitz.statistics.dto.APIResponse.ResponseError
import ru.forblitz.statistics.utils.Utils

abstract class APILoadService<A, R> {

    protected abstract val className: String

    protected var arguments: A? = null

    private var response: Response<ResponseBody>? = null

    var json: String? = null
        protected set

    var data: R? = null
        protected set

    private var taskQueue: ArrayList<(R) -> Unit> = ArrayList()

    suspend fun get(args: A): R {
        arguments = args
        if (data == null) {
            try {
                response = request()
            } catch (t: Throwable) {
                var description = "request error in $className; "
                description += "arguments = ${arguments.toString()}; "
                throw RequestException("${description}message = ${t.message}")
            }
            try {
                json = responseAsJson()
            } catch (t: Throwable) {
                var description = "responseAsJson error in $className; "
                description += "arguments = ${arguments.toString()}; "
                throw JsonConvertException("${description}message = ${t.message}")
            }
            try {
                data = parse()
            } catch (t: Throwable) {
                var description = "parse error in $className; "
                description += "arguments = ${arguments.toString()}; "
                description += "json = ${json.toString()}; "
                throw ParseException("${description}message = ${t.message}")
            }

            if (data is APIResponse) {
                (data as APIResponse).error?.let { responseError ->
                    throw ServerException(responseError)
                }
            }
        }
        onEndOfLoad()
        return data!!
    }

    protected abstract suspend fun request(): Response<ResponseBody>

    protected open fun responseAsJson(): String {
        return Utils.toJson(response!!)
    }

    protected abstract fun parse(): R

    fun addTaskOnEndOfLoad(action: (R) -> Unit) {
        if (data != null) {
            action.invoke(data!!)
        } else {
            taskQueue.add(action)
        }
    }

    protected open fun onEndOfLoad() {
        taskQueue.forEach { it.invoke(data!!) }
        taskQueue.clear()
    }

    open fun clear() {
        arguments = null
        response = null
        json = null
        data = null
        taskQueue.clear()
    }

    abstract class APILoadException(description: String): Exception(description)

    class RequestException(description: String): APILoadException(description)

    class JsonConvertException(description: String): APILoadException(description)

    class ParseException(description: String): APILoadException(description)

    data class ServerException(val responseError: ResponseError) : Exception()

}