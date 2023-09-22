package com.arefdev.base.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.utils.isOsAtLeast

/**
 * Updated on 21/09/2023
 *
 * @author [Aref Bahreini](https://github.com/arefbhrn)
 */
class HeadphoneReceiver : BroadcastReceiver() {

    var listener: HeadphoneChangeListener? = null

    interface HeadphoneChangeListener {
        fun onChanged(onHeadphone: Boolean)
    }

    override fun onReceive(context: Context, intent: Intent) {
        var onHeadphone = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (isOsAtLeast(SDK_CODES.M)) {
            val audioDevices: Array<AudioDeviceInfo> = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (audioDevice in audioDevices) {
                val type = audioDevice.type
                onHeadphone = type == AudioDeviceInfo.TYPE_WIRED_HEADSET
                        || type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || isOsAtLeast(SDK_CODES.P) && type == AudioDeviceInfo.TYPE_HEARING_AID
                        || isOsAtLeast(SDK_CODES.O) && type == AudioDeviceInfo.TYPE_USB_HEADSET
                        || isOsAtLeast(SDK_CODES.S) && type == AudioDeviceInfo.TYPE_BLE_HEADSET
            }
        }
        onHeadphone = onHeadphone
                || intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY
                || intent.action == AudioManager.ACTION_HEADSET_PLUG
                || intent.action == AudioManager.ACTION_HDMI_AUDIO_PLUG

        listener?.onChanged(onHeadphone)
    }
}
