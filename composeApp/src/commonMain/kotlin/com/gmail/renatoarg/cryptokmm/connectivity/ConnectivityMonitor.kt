package com.gmail.renatoarg.cryptokmm.connectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityMonitor {
    val isConnected: StateFlow<Boolean>
}
