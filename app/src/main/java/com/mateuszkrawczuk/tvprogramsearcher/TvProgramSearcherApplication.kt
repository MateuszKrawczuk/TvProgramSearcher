package com.mateuszkrawczuk.tvprogramsearcher

import android.app.Application
import co.touchlab.kermit.Logger
import com.mateuszkrawczuk.tvprogramsearcher.common.di.initKoin
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.appContext
import com.mateuszkrawczuk.tvprogramsearcher.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class TvProgramSearcherApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@TvProgramSearcherApplication)
            modules(appModule)
        }
//        initKoin {
//            androidLogger()

//        }

        Logger.d { "TvProgramSearcherApplication" }
    }
}