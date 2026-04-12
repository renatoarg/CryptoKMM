package com.gmail.renatoarg.cryptokmm.di

import com.gmail.renatoarg.cryptokmm.config.getApiKey
import com.gmail.renatoarg.cryptokmm.config.isDebug
import com.gmail.renatoarg.cryptokmm.data.local.CryptoLocalDatasource
import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.data.remote.createHttpClient
import com.gmail.renatoarg.cryptokmm.data.repository.CryptoRepositoryImpl
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase
import com.gmail.renatoarg.cryptokmm.presentation.CryptoListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/** Main Koin dependency injection module. */
val appModule = module {
    // Network — API key from gradle.properties, logging per build type
    single { createHttpClient(apiKey = getApiKey(), isDebug = isDebug()) }
    singleOf(::CryptoRemoteDatasource)
    // Data
    singleOf(::CryptoLocalDatasource)
    singleOf(::CryptoRepositoryImpl) bind CryptoRepository::class
    // Domain
    factoryOf(::GetCryptoListUseCase)
    // Presentation
    viewModelOf(::CryptoListViewModel)
}
