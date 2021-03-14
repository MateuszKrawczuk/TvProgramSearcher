package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.TvProgramSearcher.common.model.Show
import com.mateuszkrawczuk.TvProgramSearcher.common.model.TvImage
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.TvMazeApi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TvProgramSearcherRepository : KoinComponent  {
    private val tvMazeApi: TvMazeApi by inject()
    private val logger: Kermit by inject()

    private val coroutineScope: CoroutineScope = MainScope()
    private val tvProgramSearcherDatabase = createDb()
    private val tvProgramSearcherQueries = tvProgramSearcherDatabase?.tvProgramSearcherQueries

    init {
        coroutineScope.launch {
            fetchAndStoreShows("")
        }
    }

    fun fetchShowsAsFlow(): Flow<List<Show>> {
        // the main reason we need to do this check is that sqldelight isn't currently
        // setup for javascript client
        return tvProgramSearcherQueries?.selectAll(mapper = {
                                                             id,
                                                             image,
                                                             name,
                                                             genres,
                                                              ->
            Show(
                genres = listOf(genres),
                id = id,
                image = TvImage(image,""),
                name = name,
            )
        })?.asFlow()?.mapToList() ?: flowOf(emptyList<Show>())
    }

    fun updateSearchList(query: String) {
        coroutineScope.launch {
            fetchAndStoreShows(query)
        }
    }

    private suspend fun fetchAndStoreShows(string: String)  {
        logger.d { "fetchAndStoreShows" }
        val result = tvMazeApi.fetchShows(string)

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        tvProgramSearcherQueries?.deleteAll()
        result.forEach {
            tvProgramSearcherQueries?.insertItem(
                it.show.id,
                it.show.image?.medium ?: "",
                it.show.name,
                it.show.genres.joinToString(),
                )
        }
    }
}


