package com.avenga.kmpdemo

class DeviceMonitor {
    private val platform = getPlatform()

    fun getSystemData(): DeviceInfo {
        return platform.getDeviceInfo()
    }
}