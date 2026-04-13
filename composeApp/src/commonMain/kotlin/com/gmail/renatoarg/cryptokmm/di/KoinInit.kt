package com.gmail.renatoarg.cryptokmm.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(platformDatabaseModule, platformConnectivityModule, databaseModule, appModule)
    }
}

// iOS
fun initKoinIos() = initKoin()