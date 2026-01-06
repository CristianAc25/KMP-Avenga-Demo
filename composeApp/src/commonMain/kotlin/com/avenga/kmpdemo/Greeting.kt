package com.avenga.kmpdemo

class Greeting {
    private val platform = getPlatform()

    fun getSystemData(): DeviceInfo {
        return platform.getDeviceInfo()
    }
}