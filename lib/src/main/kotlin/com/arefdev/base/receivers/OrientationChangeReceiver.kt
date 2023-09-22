package com.arefdev.base.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.Surface
import android.view.WindowManager
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.utils.isOsAtLeast
import timber.log.Timber

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class OrientationChangeReceiver : BroadcastReceiver() {

    private val TAG = "OrientationChangeReceiver"

    var listener: OrientationChangeListener? = null

    interface OrientationChangeListener {
        fun onChanged(orientation: Int)
    }

    override fun onReceive(context: Context, intent: Intent) {
        listener?.also {
            var orientation: Int
            val configOrientation = context.resources.configuration.orientation
            when (configOrientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    orientation = Configuration.ORIENTATION_LANDSCAPE
                    Timber.tag(TAG).i("LANDSCAPE")
                }

                Configuration.ORIENTATION_PORTRAIT -> {
                    orientation = Configuration.ORIENTATION_PORTRAIT
                    Timber.tag(TAG).i("PORTRAIT")
                }

                else -> {
                    orientation = Configuration.ORIENTATION_UNDEFINED
                    Timber.tag(TAG).i("UNDEFINED")
                }
            }

            if (orientation == Configuration.ORIENTATION_UNDEFINED) {
                val displayOrientation =
                    if (isOsAtLeast(SDK_CODES.R))
                        context.display!!.rotation
                    else
                        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.orientation
                when (displayOrientation) {
                    Surface.ROTATION_0 -> {
                        orientation = Configuration.ORIENTATION_PORTRAIT
                        Timber.tag(TAG).i("PORTRAIT")
                    }

                    Surface.ROTATION_90 -> {
                        orientation = Configuration.ORIENTATION_LANDSCAPE
                        Timber.tag(TAG).i("LANDSCAPE")
                    }

                    Surface.ROTATION_180 -> {
                        orientation = Configuration.ORIENTATION_PORTRAIT
                        Timber.tag(TAG).i("PORTRAIT")
                    }

                    Surface.ROTATION_270 -> {
                        orientation = Configuration.ORIENTATION_LANDSCAPE
                        Timber.tag(TAG).i("LANDSCAPE")
                    }
                }
            }

            it.onChanged(orientation)
        }
    }
}
