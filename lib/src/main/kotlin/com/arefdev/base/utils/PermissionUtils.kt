package com.arefdev.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object PermissionUtils {

    fun Context.hasPermission(permission: String): Boolean {
        return _hasPermission(this, permission)
    }

    fun Activity.getPermission(permission: String, requestCode: Int) {
        return _getPermission(this, permission, requestCode)
    }

    private fun check(activity: Activity, permission: String, requestCode: Int) {
        if (activity.hasPermission(permission).not()) {
            activity.getPermission(permission, requestCode)
        }
    }

    fun _hasPermission(context: Context, permission: String): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(context, permission)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    fun _getPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun getNetworkStatePermission(activity: Activity, requestCode: Int) {
        check(activity, Manifest.permission.ACCESS_NETWORK_STATE, requestCode)
    }

    fun getPhoneStatePermission(activity: Activity, requestCode: Int) {
        check(activity, Manifest.permission.READ_PHONE_STATE, requestCode)
    }

    fun getContactPermission(activity: Activity, requestCode: Int) {
        check(activity, Manifest.permission.READ_CONTACTS, requestCode)
    }
}
