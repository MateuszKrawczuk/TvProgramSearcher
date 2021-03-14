package com.mateuszkrawczuk.TvProgramSearcher.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show(
    @SerialName("genres")
    val genres: List<String>,
    val id: Long,
    val image: TvImage?,
    val name: String,
)

@Serializable
data class TvImage(
    val medium: String,
    val original: String
)