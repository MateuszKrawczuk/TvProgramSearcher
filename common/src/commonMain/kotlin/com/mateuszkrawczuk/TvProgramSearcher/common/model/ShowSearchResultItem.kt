package com.mateuszkrawczuk.TvProgramSearcher.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ShowSearchResultItem(
    val score: Double,
    val show: Show
)