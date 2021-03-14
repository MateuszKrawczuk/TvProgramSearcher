package com.mateuszkrawczuk.tvprogramsearcher.di

import android.app.Application
import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.tvprogramsearcher.common.di.initKoin
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.appContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TvProgramSearcherApplication : Application(), KoinComponent {
    private val logger: Kermit by inject()

    override fun onCreate() {
        super.onCreate()

        appContext = this

        initKoin {
            androidLogger()
            androidContext(this@TvProgramSearcherApplication)
            modules(appModule)
        }

        logger.d { "TvProgramSearcherApplication" }
    }
}