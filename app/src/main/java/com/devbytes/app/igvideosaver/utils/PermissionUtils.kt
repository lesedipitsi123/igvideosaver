package com.devbytes.app.igvideosaver.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtils(private val activity: Activity){

    fun requestPermission(requestCode : Int, permissions: Array<String>) {
        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
    }

    private fun hasPermissions (permissions: Array<String>) : Boolean {
        permissions.forEach { permission ->
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED ) {
                return false
            }
        }

        return true
    }
}