package com.arefdev.base.repo.network.retrofit

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import kotlin.math.min

/**
 * Updated on 06/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class PrettyLogger : HttpLoggingInterceptor.Logger {

    companion object {
        private const val TAG = "OkHttpLogger"
    }

    enum class LogLevel {
        INFO,
        WARNING,
        DEBUG,
        ERROR,
    }

    override fun log(message: String) {
        if (!message.startsWith("{") && !message.startsWith("[")) {
            logLongMessage(message, LogLevel.DEBUG)
            return
        }
        try {
            val prettyPrintJson = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(message))
            logLongMessage("‌\n$prettyPrintJson", LogLevel.DEBUG)
        } catch (m: JsonSyntaxException) {
            logLongMessage(message, LogLevel.DEBUG)
        }
    }

    private fun logLongMessage(message: String, logLevel: LogLevel) {
        val maxLogSize = 1000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = min(end, message.length)
            when (logLevel) {
                LogLevel.INFO -> Timber.tag(TAG).i(message.substring(start, end))
                LogLevel.DEBUG -> Timber.tag(TAG).d(message.substring(start, end))
                LogLevel.WARNING -> Timber.tag(TAG).w(message.substring(start, end))
                LogLevel.ERROR -> Timber.tag(TAG).e(message.substring(start, end))
            }
        }
    }
}