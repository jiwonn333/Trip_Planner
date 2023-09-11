package com.example.letstravel.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager {

    enum class AppPermission(val permissionName: String) {
        FINE_LOCATION(android.Manifest.permission.ACCESS_FINE_LOCATION),
    }

    fun isPermissionGranted(context: Context, appPermission: AppPermission): Boolean {
        return ContextCompat.checkSelfPermission(context, appPermission.permissionName) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, requestCode: Int, vararg permissions: AppPermission) {
        val permissionNames = permissions.map {
            it.permissionName
        }.toTypedArray()

        ActivityCompat.requestPermissions(
            activity, permissionNames,
            requestCode
        )
    }

}