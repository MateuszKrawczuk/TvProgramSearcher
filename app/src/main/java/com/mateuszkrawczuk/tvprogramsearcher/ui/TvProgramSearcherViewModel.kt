package com.mateuszkrawczuk.tvprogramsearcher.ui

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.TvProgramSearcherRepository

class TvProgramSearcherViewModel(
    private val tvProgramSearcherRepository: TvProgramSearcherRepository,
    private val logger: Kermit
) : ViewModel()