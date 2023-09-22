package com.arefdev.base.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.utils.NetworkUtils.isOnline
import com.arefdev.base.utils.isOsAtLeast
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * Updated on 13/08/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object NetworkChangeReceiver : BroadcastReceiver() {

    private val INTENT_FILTER = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

    val isOnline = MutableLiveData<Boolean>()
    val isOnlinePub = BehaviorSubject.create<Boolean>()

    private var started = false

    fun init(context: Context) {
        if (started)
            return

        if (isOsAtLeast(SDK_CODES.TIRAMISU)) {
            context.registerReceiver(this, INTENT_FILTER, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(this, INTENT_FILTER)
        }
        started = true
    }

    override fun onReceive(context: Context, intent: Intent) {
        isOnline(context).let {
            isOnline.postValue(it)
            isOnlinePub.onNext(it)
        }
    }
}
