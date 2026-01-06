package com.avenga.kmpdemo

import android.content.res.Resources
import android.os.Build

class AndroidPlatform : Platform {
    override fun getDeviceInfo(): DeviceInfo {
        val displayMetrics = Resources.getSystem().displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        return DeviceInfo(
            osName = "Android",
            osVersion = "SDK ${Build.VERSION.SDK_INT}",
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            screenResolution = "${width}x${height} px"
        )
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()