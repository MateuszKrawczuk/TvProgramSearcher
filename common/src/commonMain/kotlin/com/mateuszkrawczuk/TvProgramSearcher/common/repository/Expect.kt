package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import co.touchlab.kermit.Logger
import com.mateuszkrawczuk.tvprogramsearcher.db.TvProgramSearcherDatabase

expect fun createDb() : TvProgramSearcherDatabase?

expect fun getLogger(): Logger