package com.gmail.renatoarg.cryptokmm.di

import com.gmail.renatoarg.cryptokmm.connectivity.AndroidConnectivityMonitor
import com.gmail.renatoarg.cryptokmm.connectivity.ConnectivityMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformConnectivityModule = module {
    single { AndroidConnectivityMonitor(androidContext()) } bind ConnectivityMonitor::class
}
