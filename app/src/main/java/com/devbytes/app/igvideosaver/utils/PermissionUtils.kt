package com.devbytes.app.igvideosaver.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PermissionUtils @Inject constructor(private val activity: Activity){

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