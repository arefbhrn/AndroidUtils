package com.arefdev.base.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class NotificationDismissReceiver : BroadcastReceiver() {

    companion object {

        const val EXTRA_NOTIFICATION_ID = "notification_id"

        fun create(context: Context, notificationId: Int): Intent {
            val intent = Intent(context, NotificationDismissReceiver::class.java)
            intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            return intent
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}