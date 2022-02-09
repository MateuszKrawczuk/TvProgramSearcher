package com.mateuszkrawczuk.tvprogramsearcher.di

import com.mateuszkrawczuk.TvProgramSearcher.common.di.createDbClient
import com.mateuszkrawczuk.tvprogramsearcher.ui.TvProgramSearcherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.AndroidDatabaseDependencyProvider

val appModule = module {
    viewModel { TvProgramSearcherViewModel(get()) }
    single { createDbClient(AndroidDatabaseDependencyProvider(get())) }
}
