package com.arefdev.base.repo

import android.accounts.AccountManager.KEY_PASSWORD
import android.content.Context
import com.arefdev.base.BaseApp
import com.arefdev.base.extensions.subscribeOnIOThread
import io.reactivex.rxjava3.core.Completable

/**
 * Updated on 16/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object SharedPrefs {

    private val prefs = BaseApp.context.getSharedPreferences("main", Context.MODE_PRIVATE)

    private const val KEY_TOKEN = "token"
    private const val KEY_FIRST_RUN = "first_run"

    private fun getSharedPrefs(key: String): String? {
        return prefs.getString(key, null)
    }

    private fun putSharedPrefs(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    @JvmStatic
    fun saveFirstRun() {
        putSharedPrefs(KEY_FIRST_RUN, "false")
    }

    @JvmStatic
    val isFirstRun: Boolean
        get() {
            return try {
                getSharedPrefs(KEY_FIRST_RUN)!!.toBoolean()
            } catch (e: Exception) {
                true
            }
        }

    @JvmStatic
    fun saveToken(token: String?) {
        putSharedPrefs(KEY_TOKEN, token)
    }

    @JvmStatic
    val token: String?
        get() {
            return try {
                getSharedPrefs(KEY_TOKEN)
            } catch (e: Exception) {
                null
            }
        }

    @JvmStatic
    val isLoggedIn: Boolean
        get() = token.isNullOrBlank().not()

    @JvmStatic
    fun logout(): Completable {
        return Completable.fromRunnable {
            prefs.edit().remove(KEY_PASSWORD).apply()
            prefs.edit().remove(KEY_TOKEN).apply()
        }.subscribeOnIOThread()
    }
}
