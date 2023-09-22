package com.arefdev.base.utils

import android.app.Activity
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object DeviceUtils {

    val osApiVersion: Int
        /**
         * @return OS API version(ex: 16, 28, etc.)
         */
        get() = Build.VERSION.SDK_INT

    val osVersionName: String
        /**
         * @return OS version name (ex: JELLY_BEAN, O, P, etc.)
         */
        get() {
            val fields = VERSION_CODES::class.java.fields
            for (mField in fields) {
                val fieldName = mField.name
                var fieldValue = -1
                try {
                    fieldValue = mField.getInt(Any())
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                if (fieldValue == Build.VERSION.SDK_INT) {
                    return fieldName
                }
            }
            return "UNKNOWN"
        }

    val osReleaseVersion: String
        /**
         * @return OS release version (ex: 4.1.1, 7.0.1, etc.)
         */
        get() = Build.VERSION.RELEASE

    val deviceFullName: String
        /**
         * @return the consumer friendly device full name
         */
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model
            } else "$manufacturer $model"
        }

    val deviceManufacturerName: String
        /**
         * @return the consumer friendly device manufacturer name
         */
        get() = Build.MANUFACTURER

    val deviceName: String
        /**
         * @return the consumer friendly device name
         */
        get() = Build.MODEL

    fun hasSoftNavigationBar(activity: Activity): Boolean {
        val hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }
}
