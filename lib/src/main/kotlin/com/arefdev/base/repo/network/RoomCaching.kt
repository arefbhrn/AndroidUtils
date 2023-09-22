package com.arefdev.base.repo.network

import com.arefdev.base.BaseApp
import com.arefdev.base.extensions.subscribeOnIOThread
import com.arefdev.base.extensions.subscribeVia
import com.arefdev.base.repo.db.DBHolder
import com.arefdev.base.repo.db.netcache.RoomNetCache
import com.arefdev.base.repo.network.retrofit.RetrofitResponse
import com.arefdev.base.utils.NetworkUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Completable
import org.json.JSONArray
import org.json.JSONObject

/**
 * Updated on 13/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object RoomCaching {

    @JvmStatic
    fun check(url: String, offlineCallback: (String) -> Unit, onlineCallback: () -> Unit) {
        if (NetworkUtils.isOnline(BaseApp.context).not()) {
            DBHolder.netCaches[url]
                .flatMapCompletable {
                    Completable.fromRunnable {
                        if (it.statusCode in 200 until 300)
                            offlineCallback(it.response!!)
                        else
                            onlineCallback()
                    }
                }
                .doOnError {
                    onlineCallback()
                }.subscribeOnIOThread()
                .subscribeVia()
        } else {
            onlineCallback()
        }
    }

    @JvmStatic
    fun get(url: String, callback: (String?) -> Unit) {
        DBHolder.netCaches[url]
            .flatMapCompletable {
                Completable.fromRunnable {
                    if (it.statusCode in 200 until 300)
                        callback(it.response)
                    else
                        callback(null)
                }
            }.doOnError {
                callback(null)
            }.subscribeOnIOThread()
            .subscribeVia()
    }

    @JvmStatic
    fun <T> get(url: String, tClass: Class<T>, callback: (T?) -> Unit) {
        DBHolder.netCaches[url]
            .flatMapCompletable {
                Completable.fromRunnable {
                    if (it.statusCode in 200 until 300)
                        callback(Gson().fromJson(it.response, TypeToken.get(tClass)))
                    else
                        callback(null)
                }
            }.doOnError {
                callback(null)
            }.subscribeOnIOThread()
            .subscribeVia()
    }

    @JvmStatic
    fun <T> get(url: String, typeToken: TypeToken<T>, callback: (T?) -> Unit) {
        DBHolder.netCaches[url]
            .flatMapCompletable {
                Completable.fromRunnable {
                    if (it.statusCode in 200 until 300)
                        callback(Gson().fromJson(it.response, typeToken.type))
                    else
                        callback(null)
                }
            }.doOnError {
                callback(null)
            }.subscribeOnIOThread()
            .subscribeVia()
    }

    private fun store(url: String, statusCode: Int, data: String) {
        DBHolder.netCaches.insert(RoomNetCache(url, System.currentTimeMillis(), statusCode, data))
            .subscribeVia()
    }

    @JvmStatic
    fun store(url: String, response: JSONArray) {
        store(url, 200, response.toString())
    }

    @JvmStatic
    fun store(url: String, response: JSONObject) {
        store(url, 200, response.toString())
    }

    @JvmStatic
    fun store(url: String, response: JsonArray) {
        store(url, 200, response.toString())
    }

    @JvmStatic
    fun store(url: String, response: JsonObject) {
        store(url, 200, response.toString())
    }

    @JvmStatic
    fun store(url: String, response: String) {
        store(url, 200, response)
    }

    @JvmStatic
    fun store(url: String, response: RetrofitResponse<*>) {
        store(url, response.statusCode, Gson().toJson(response.data))
    }

    @JvmStatic
    fun store(url: String, response: Any) {
        store(url, 200, Gson().toJson(response))
    }
}
