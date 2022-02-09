package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import co.touchlab.kermit.Logger
import com.mateuszkrawczuk.TvProgramSearcher.common.model.Show
import com.mateuszkrawczuk.TvProgramSearcher.common.model.TvImage
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.TvMazeApi
import com.mateuszkrawczuk.tvprogramsearcher.db.TvProgramSearcherDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


interface TvProgramSearcherRepositoryInterface {
    fun fetchShowsAsFlow(): Flow<List<Show>>
    fun updateSearchList(query: String)
}

class TvProgramSearcherRepository : KoinComponent, TvProgramSearcherRepositoryInterface {
    private val tvMazeApi: TvMazeApi by inject()


    private val coroutineScope: CoroutineScope = MainScope()
    private val tvProgramSearcherDatabase: TvProgramSearcherDatabase? by inject()
    private val tvProgramSearcherQueries = tvProgramSearcherDatabase?.tvProgramSearcherQueries

    val logger = Logger.withTag("TvProgramSearcher")

    init {
        coroutineScope.launch {
            fetchAndStoreShows("")
        }
    }

    override fun fetchShowsAsFlow(): Flow<List<Show>> {
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
                image = TvImage(image, ""),
                name = name,
            )
        })?.asFlow()?.mapToList() ?: flowOf(emptyList<Show>())
    }

    override fun updateSearchList(query: String) {
        coroutineScope.launch {
            fetchAndStoreShows(query)
        }
    }

    private suspend fun fetchAndStoreShows(string: String) {
        logger.d { "fetchAndStoreShows" }
        val result = tvMazeApi.fetchShows(string)

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        tvProgramSearcherQueries?.transaction {
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
}


