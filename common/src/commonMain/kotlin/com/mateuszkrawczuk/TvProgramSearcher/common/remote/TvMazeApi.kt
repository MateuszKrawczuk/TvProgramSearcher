package com.mateuszkrawczuk.tvprogramsearcher.common.remote

import com.mateuszkrawczuk.TvProgramSearcher.common.model.ShowSearchResultItem
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent

class TvMazeApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.tvmaze.com",
) : KoinComponent {
    suspend fun fetchShows(query: String) = client.get<ArrayList<ShowSearchResultItem>>("$baseUrl/search/shows?q=$query")
}
