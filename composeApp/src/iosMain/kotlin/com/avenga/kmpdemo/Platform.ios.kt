package com.avenga.kmpdemo

import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

class IOSPlatform: Platform {

    @OptIn(ExperimentalForeignApi::class)
    override fun getDeviceInfo(): DeviceInfo {
        val device = UIDevice.currentDevice
        val screenBounds = UIScreen.mainScreen.bounds

        val width = screenBounds.useContents { size.width }.toInt()
        val height = screenBounds.useContents { size.height }.toInt()

        return DeviceInfo(
            osName = device.systemName,
            osVersion = device.systemVersion,
            deviceModel = device.model,
            screenResolution = "${width}x${height} pts"
        )
    }
}

actual fun getPlatform(): Platform = IOSPlatform()