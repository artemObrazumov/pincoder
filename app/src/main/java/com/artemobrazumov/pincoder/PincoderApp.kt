package com.artemobrazumov.pincoder

import android.app.Application
import com.artemobrazumov.pincoder.di.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PincoderApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PincoderApp)
            modules(KoinModule)
        }
    }
}