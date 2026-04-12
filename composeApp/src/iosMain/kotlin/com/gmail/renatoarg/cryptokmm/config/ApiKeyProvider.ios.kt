package com.gmail.renatoarg.cryptokmm.config

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

// TODO: Read API key from Info.plist or a secrets manager
/** Returns the API key for iOS. Currently empty (free tier). */
actual fun getApiKey(): String = ""

/** Returns true for debug builds via Kotlin/Native platform flag. */
@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary
