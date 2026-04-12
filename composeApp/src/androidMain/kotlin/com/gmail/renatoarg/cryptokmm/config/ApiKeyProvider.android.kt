package com.gmail.renatoarg.cryptokmm.config

import com.gmail.renatoarg.cryptokmm.BuildConfig

/** Reads the API key from Android BuildConfig (set in gradle.properties). */
actual fun getApiKey(): String = BuildConfig.API_KEY

/** Returns true for debug builds via Android BuildConfig. */
actual fun isDebug(): Boolean = BuildConfig.DEBUG
