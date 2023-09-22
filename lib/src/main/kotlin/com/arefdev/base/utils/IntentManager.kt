package com.arefdev.base.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import com.arefdev.base.R

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object IntentManager {

    fun restartApp(activity: Activity) {
        val i = activity.packageManager.getLaunchIntentForPackage(activity.baseContext.packageName)!!
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(i)
    }

    fun shareText(context: Context, title: String?, body: String?) {
        val i = Intent(Intent.ACTION_SEND)
        i.setType("text/plain")
        i.putExtra(Intent.EXTRA_SUBJECT, title)
        i.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(i, context.getString(R.string.share)))
    }

    fun openURL(context: Context, url: String?) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (ignored: Exception) {
        }
    }

    fun openDialer(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        try {
            context.startActivity(intent)
        } catch (ignored: Exception) {
        }
    }

    fun copyStringToClipBoard(context: Context, text: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(text, text)
        clipboard.setPrimaryClip(clip)
    }

    fun wakeScreen(context: Context) {
        try {
            val powerManager = (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
            val wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "Tag:")
            wakeLock.acquire(200)
            wakeLock.release()
        } catch (ignored: Exception) {
        }
    }

    fun exitApp(activity: Activity) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}
