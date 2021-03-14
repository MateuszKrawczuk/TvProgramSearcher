package com.mateuszkrawczuk.tvprogramsearcher.di

import com.mateuszkrawczuk.tvprogramsearcher.ui.TvProgramSearcherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TvProgramSearcherViewModel(get(),get()) }
}
