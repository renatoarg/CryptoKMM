package com.gmail.renatoarg.cryptokmm.di

import com.gmail.renatoarg.cryptokmm.connectivity.ConnectivityMonitor
import com.gmail.renatoarg.cryptokmm.connectivity.IosConnectivityMonitor
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformConnectivityModule = module {
    single { IosConnectivityMonitor() } bind ConnectivityMonitor::class
}
