package com.gmail.renatoarg.cryptokmm.connectivity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

class IosConnectivityMonitor : ConnectivityMonitor {

    private val monitor = nw_path_monitor_create()
    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected

    init {
        val queue = dispatch_queue_create("connectivity_monitor", null)
        nw_path_monitor_set_update_handler(monitor) { path ->
            _isConnected.value = nw_path_get_status(path) == nw_path_status_satisfied
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }
}
