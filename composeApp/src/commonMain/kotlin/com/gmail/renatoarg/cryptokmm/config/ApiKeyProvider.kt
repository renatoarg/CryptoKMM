package com.gmail.renatoarg.cryptokmm.config

/** Returns the CoinGecko API key configured per platform. */
expect fun getApiKey(): String

/** Returns true when running a debug build. */
expect fun isDebug(): Boolean
