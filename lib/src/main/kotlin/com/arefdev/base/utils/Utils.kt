package com.arefdev.base.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.arefdev.base.BaseApp
import com.arefdev.base.extensions.observeOnMainThread
import com.arefdev.base.extensions.subscribeOnMainThread
import com.arefdev.base.extensions.subscribeOnSingleThread
import com.arefdev.base.extensions.subscribeVia
import com.arefdev.base.model.Time
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.io.DataOutputStream
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

interface Callback<T> {
    fun onCall(t: T)
}

fun generateCreateTime(): String {
    val cal = Calendar.getInstance()
    val date = (cal[Calendar.YEAR]
        .toString() + "-"
            + String.format(Locale.ENGLISH, "%02d", cal[Calendar.MONTH] + 1) + "-"
            + String.format(Locale.ENGLISH, "%02d", cal[Calendar.DAY_OF_MONTH]))
    val time = (String.format(Locale.ENGLISH, "%02d", cal[Calendar.HOUR_OF_DAY]) + ":"
            + String.format(Locale.ENGLISH, "%02d", cal[Calendar.MINUTE]) + ":"
            + String.format(Locale.ENGLISH, "%02d", cal[Calendar.SECOND]))
    return "$date $time"
}

fun showToast(context: Context?, message: CharSequence?, duration: Int) {
    Toast.makeText(context, message, duration).show()
}

fun showSnackbar(context: Context?, message: CharSequence?, durationMs: Int): Snackbar? {
    return if (context is Activity) {
        showSnackbar(context, message, durationMs)
    } else {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        null
    }
}

fun showSnackbar(activity: Activity, message: CharSequence?, durationMs: Int): Snackbar {
    return showSnackbar(activity.findViewById<View>(android.R.id.content), message, durationMs)
}

fun showSnackbar(view: View?, message: CharSequence?, durationMs: Int): Snackbar {
    val snackbar = Snackbar.make(view!!, message!!, durationMs)
    ViewCompat.setLayoutDirection(snackbar.view, ViewCompat.LAYOUT_DIRECTION_RTL)
    snackbar.show()
    return snackbar
}

fun dpToPx(px: Float): Float {
    return px * BaseApp.context.resources.displayMetrics.density
}

fun pxToDp(dp: Float): Float {
    return dp / BaseApp.context.resources.displayMetrics.density
}

fun changeSystemTime(year: String, month: String, day: String, hour: String, minute: String, second: String) {
    try {
        val process = Runtime.getRuntime().exec("su")
        val os = DataOutputStream(process.outputStream)
        val command = "date -s $year$month$day.$hour$minute$second\n"
        Timber.e("command: %s", command)
        os.writeBytes(command)
        os.flush()
        os.writeBytes("exit\n")
        os.flush()
        process.waitFor()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun interval(millis: Long): Observable<Long> {
    return interval(millis, TimeUnit.MILLISECONDS)
}

fun interval(period: Long, unit: TimeUnit): Observable<Long> {
    return Observable.interval(period, unit)
        .subscribeOnSingleThread()
}

fun interval(initialDelay: Long, period: Long, unit: TimeUnit): Observable<Long> {
    return Observable.interval(initialDelay, period, unit)
        .subscribeOnSingleThread()
}

fun interval(period: Time): Observable<Long> {
    return interval(period.time, period.unit)
}

fun interval(initialDelay: Long, period: Time): Observable<Long> {
    return interval(initialDelay, period.time, period.unit)
}

fun timer(millis: Long): Completable {
    return timer(millis, TimeUnit.MILLISECONDS)
}

fun timer(period: Long, unit: TimeUnit): Completable {
    return Completable.timer(period, unit)
        .subscribeOnSingleThread()
}

fun timer(period: Time): Completable {
    return timer(period.time, period.unit)
}

fun log(msg: String) {
    log(null, msg)
}

fun log(tag: String? = null, msg: String) {
    logD(tag, msg)
}

fun logD(tag: String? = null, msg: String) {
    (tag?.let { Timber.tag(tag) } ?: Timber)
        .d(msg)
}

fun logI(tag: String? = null, msg: String) {
    (tag?.let { Timber.tag(tag) } ?: Timber)
        .i(msg)
}

fun logW(tag: String? = null, msg: String) {
    (tag?.let { Timber.tag(tag) } ?: Timber)
        .w(msg)
}

fun logE(tag: String? = null, msg: String) {
    (tag?.let { Timber.tag(tag) } ?: Timber)
        .e(msg)
}

fun logE(tag: String? = null, msg: String, e: Throwable? = null) {
    (tag?.let { Timber.tag(tag) } ?: Timber)
        .e(e, msg)
}

fun runOnMainThread(unit: () -> Unit) {
    Completable.fromRunnable { unit() }
        .subscribeOnMainThread()
        .observeOnMainThread()
        .subscribeVia()
}

fun tryCatch(_try: () -> Unit, _catch: ((e: Exception) -> Unit)? = null) {
    try {
        _try()
    } catch (e: Exception) {
        _catch?.invoke(e)
    }
}

fun isOnMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
            || Looper.getMainLooper().thread == Thread.currentThread()
}

fun isOsAtLeast(sdk: Int): Boolean {
    return Build.VERSION.SDK_INT >= sdk
}

/**
 * Generates a new [ColorStateList] from a color which is usable as tint
 *
 * @param color Color value
 * @return New [ColorStateList] object
 */
fun generateColorStateList(color: Int): ColorStateList {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_enabled),  // enabled
        intArrayOf(-android.R.attr.state_enabled), // disabled
        intArrayOf(-android.R.attr.state_checked), // unchecked
        intArrayOf(android.R.attr.state_pressed),  // pressed
    )
    val colors = intArrayOf(
        color,
        color,
        color,
        color
    )
    return ColorStateList(states, colors)
}
