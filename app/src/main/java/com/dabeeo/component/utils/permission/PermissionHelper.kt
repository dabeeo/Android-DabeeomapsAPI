package com.dabeeo.component.utils.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHelper {

    companion object {
        fun hasPermission(activity: Activity,
                            permissions: Array<String>) : Boolean {
            var hasPermission = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activity!!,
                        permission
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    hasPermission = false
                    break
                }
            }
            return hasPermission
        }
    }

}