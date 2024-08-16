package com.artemobrazumov.pincoder.di

import com.artemobrazumov.pincoder.screens.confirm.ConfirmViewModel
import com.artemobrazumov.pincoder.screens.main.MainViewModel
import com.artemobrazumov.pincoder.shared_preferences.SharedPreferencesRepository
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val KoinModule = module {
    factoryOf(::SharedPreferencesRepository)

    singleOf(::Gson)

    viewModel { MainViewModel(get()) }
    viewModelOf(::MainViewModel)
    viewModel { ConfirmViewModel(get()) }
    viewModelOf(::ConfirmViewModel)
}