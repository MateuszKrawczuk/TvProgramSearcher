package com.mateuszkrawczuk.TvProgramSearcher.common.remote

import kotlinx.serialization.Serializable

@Serializable
data class ShowSearchResultItem(
    val score: Double,
    val show: Show
)