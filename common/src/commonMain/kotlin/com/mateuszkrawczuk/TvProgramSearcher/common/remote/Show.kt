package com.mateuszkrawczuk.TvProgramSearcher.common.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show(
    @SerialName("genres")
    val genres: List<String>,
    val id: Long,
    val image: ByteArray?,
    val language: String,
    val name: String,
    val officialSite: String,
    val premiered: String,
    val summary: String,
//    val url: String,
)