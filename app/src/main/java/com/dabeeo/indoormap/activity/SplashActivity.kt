package com.dabeeo.indoormap.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.dabeeo.component.utils.intents.IntentUtils
import com.dabeeo.component.utils.permission.PermissionHelper
import com.dabeeo.indoormap.BaseActivity
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.ActivitySplashBinding

class SplashActivity: BaseActivity<ActivitySplashBinding>() {

    private val INTENT_DELAY: Long = 2000
    private val REQUEST_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_PERMISSIONS_CODE = 1000

    override fun getLayoutId(): Int {
        return ProjectLayout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val isPermissionGranted = PermissionHelper.hasPermission(this, REQUEST_PERMISSIONS)
        if(isPermissionGranted) {
            intentLogin()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUEST_PERMISSIONS, REQUEST_PERMISSIONS_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> if (grantResults.isNotEmpty() && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                intentLogin()
            } else {
                back(IntentUtils.EFFECT.SLIDE_LEFT_RIGHT)
            }
        }
    }

    private fun intentLogin() {
        val handlerOnMainThread = Handler(Looper.getMainLooper())
        handlerOnMainThread.postDelayed({
            intent(LoginActivity::class.java, IntentUtils.EFFECT.SLIDE_LEFT_RIGHT)
        }, INTENT_DELAY)
    }

}