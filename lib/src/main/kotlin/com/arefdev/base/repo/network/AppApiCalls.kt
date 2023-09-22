package com.arefdev.base.repo.network

import com.arefdev.base.repo.network.retrofit.APIService
import com.arefdev.base.repo.network.retrofit.RetrofitRequest
import com.arefdev.base.utils.CalendarTool
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeoutException

/**
 * Updated on 25/05/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object AppApiCalls {

    @JvmField
    var OFFLINE_MODE = false

    private val service: APIService by lazy {
        RetrofitRequest<Void>(
            cacheEnabled = false,
            cacheAge = 60
        ).getApiService(APIService::class.java)
    }

    private fun <T : Any> notLoggedInError() = Single.error<T>(Exception("Not logged in"))

    private fun <T : Any> offlineError() = Single.error<T>(TimeoutException())

    @JvmStatic
    fun getTime(): Single<JsonObject> {
        return service.getTime(CalendarTool.now().calendar.timeZone.id).let { api ->
            RetrofitRequest<JsonObject>()
                .setApi(api)
                .map { response -> response.data }
        }
    }

    @JvmStatic
    fun getTimeAsCalendarTool(): Single<CalendarTool> {
        return getTime()
            .map { response ->
                val datetime = response["datetime"].asString
                CalendarTool(
                    datetime.substring(0, 4).toInt(),
                    datetime.substring(5, 7).toInt(),
                    datetime.substring(8, 10).toInt(),
                    datetime.substring(11, 13).toInt(),
                    datetime.substring(14, 16).toInt(),
                    datetime.substring(17, 19).toInt(),
                )
            }
    }
}
