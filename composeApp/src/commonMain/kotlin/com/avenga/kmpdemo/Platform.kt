package com.avenga.kmpdemo

interface Platform {
    fun getDeviceInfo(): DeviceInfo
}

expect fun getPlatform(): Platform