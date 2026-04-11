package com.gmail.renatoarg.cryptokmm.di

import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import com.gmail.renatoarg.cryptokmm.data.repository.CryptoRepositoryImpl
import com.gmail.renatoarg.cryptokmm.domain.repository.CryptoRepository
import com.gmail.renatoarg.cryptokmm.domain.usecase.GetCryptoListUseCase
import com.gmail.renatoarg.cryptokmm.presentation.CryptoListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::CryptoRemoteDatasource)
    singleOf(::CryptoRepositoryImpl) bind CryptoRepository::class
    factoryOf(::GetCryptoListUseCase)
    viewModelOf(::CryptoListViewModel)
}