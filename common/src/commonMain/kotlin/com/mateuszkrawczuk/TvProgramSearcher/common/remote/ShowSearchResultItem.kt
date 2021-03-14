package com.mateuszkrawczuk.TvProgramSearcher.common.remote

import com.mateuszkrawczuk.TvProgramSearcher.common.model.Show
import kotlinx.serialization.Serializable

@Serializable
data class ShowSearchResultItem(
    val score: Double,
    val show: Show
)