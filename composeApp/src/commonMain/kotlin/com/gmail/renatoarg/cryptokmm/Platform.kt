package com.gmail.renatoarg.cryptokmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform