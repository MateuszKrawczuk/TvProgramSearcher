package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import android.content.Context
import com.mateuszkrawczuk.TvProgramSearcher.common.di.IDatabaseDependencyProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.mateuszkrawczuk.tvprogramsearcher.db.TvProgramSearcherDatabase

lateinit var appContext: Context

actual fun createDb(dependencyProvider: IDatabaseDependencyProvider): TvProgramSearcherDatabase? {
    return if(dependencyProvider is AndroidDatabaseDependencyProvider) {
        val driver =
            AndroidSqliteDriver(TvProgramSearcherDatabase.Schema, dependencyProvider.context, "peopleinspace.db")
        TvProgramSearcherDatabase(driver)
    } else null

//    val driver = AndroidSqliteDriver(TvProgramSearcherDatabase.Schema, appContext, "tvprogramsearcher.db")
//    return TvProgramSearcherDatabase(driver)
}

//actual fun getLogger(): Logger = LogcatLogger()
class AndroidDatabaseDependencyProvider(val context: Context):IDatabaseDependencyProvider