package com.avenga.kmpdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform