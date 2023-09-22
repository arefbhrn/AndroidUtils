package com.arefdev.base.utils

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber
import java.util.Locale

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class TimberLogTree(private val isDebugMode: Boolean) : Timber.Tree() {

    companion object {
        private const val DEFAULT_TAG = "TimberLogTree"
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var mTag = tag
        if (!isDebugMode) {
            return
        }
        mTag = mTag ?: DEFAULT_TAG
        when (priority) {
            Log.VERBOSE -> Log.v(mTag, message)
            Log.DEBUG -> Log.d(mTag, message)
            Log.INFO -> Log.i(mTag, message)
            Log.WARN -> Log.w(mTag, message)
            Log.ERROR -> Log.e(mTag, message)
        }
    }

    override fun formatMessage(message: String, args: Array<out Any?>): String {
        return String.format(Locale.ENGLISH, message, *args)
    }
}
