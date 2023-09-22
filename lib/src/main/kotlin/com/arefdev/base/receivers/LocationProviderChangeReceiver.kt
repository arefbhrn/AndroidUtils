package com.arefdev.base.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.extensions.sendValue

/**
 * Updated on 21/09/2023
 *
 * @author [Aref Bahreini](https://github.com/arefbhrn)
 */
class LocationProviderChangeReceiver : BroadcastReceiver() {

    companion object {

        val INTENT_FILTER = IntentFilter("android.location.PROVIDERS_CHANGED")

        private val isAvailable = MutableLiveData(false)
        private val isGpsEnabled = MutableLiveData(false)
        val isNetworkEnabled = MutableLiveData(false)

        fun getNewInstance(context: Context): LocationProviderChangeReceiver {
            val receiver = LocationProviderChangeReceiver()
            context.registerReceiver(receiver, INTENT_FILTER)
            return receiver
        }
    }

    private var locationManager: LocationManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (locationManager == null)
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        isAvailable.sendValue(isGpsEnabled && isNetworkEnabled)
        Companion.isGpsEnabled.sendValue(isGpsEnabled)
        isAvailable.sendValue(isNetworkEnabled)
    }
}