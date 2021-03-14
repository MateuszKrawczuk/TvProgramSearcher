package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import android.content.Context
import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.mateuszkrawczuk.tvprogramsearcher.db.TvProgramSearcherDatabase

lateinit var appContext: Context

actual fun createDb(): TvProgramSearcherDatabase? {
    val driver = AndroidSqliteDriver(TvProgramSearcherDatabase.Schema, appContext, "tvprogramsearcher.db")
    return TvProgramSearcherDatabase(driver)
}

actual fun getLogger(): Logger = LogcatLogger()