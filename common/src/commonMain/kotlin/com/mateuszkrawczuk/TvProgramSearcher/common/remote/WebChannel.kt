package com.mateuszkrawczuk.TvProgramSearcher.common.remote

import kotlinx.serialization.Serializable

@Serializable
data class WebChannel(
    val country: String,
    val id: Int,
    val name: String
)