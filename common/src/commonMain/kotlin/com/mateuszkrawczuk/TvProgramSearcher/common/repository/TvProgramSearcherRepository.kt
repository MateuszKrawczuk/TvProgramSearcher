package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.TvProgramSearcher.common.remote.Show
import com.mateuszkrawczuk.tvprogramsearcher.common.model.personBios
import com.mateuszkrawczuk.tvprogramsearcher.common.model.personImages
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.TvMazeApi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext


class TvProgramSearcherRepository : KoinComponent  {
    private val tvMazeApi: TvMazeApi by inject()
    private val logger: Kermit by inject()

    private val coroutineScope: CoroutineScope = MainScope()
    private val tvProgramSearcherDatabase = createDb()
    private val tvProgramSearcherQueries = tvProgramSearcherDatabase?.tvProgramSearcherQueries

    var peopleJob: Job? = null

    init {
        coroutineScope.launch {
            fetchAndStorePeople()
        }
    }

    fun fetchPeopleAsFlow(): Flow<List<Show>> {
        // the main reason we need to do this check is that sqldelight isn't currently
        // setup for javascript client
        return tvProgramSearcherQueries?.selectAll(mapper = {
                                                             id,
                                                             image,
                                                             language,
                                                             name,
                                                             officialSite,
                                                             premiered,
                                                             genres,
                                                             summary
                                                              ->
            Show(
                genres = listOf(genres),
                id = id,
                image = image,
                language = language,
                name = name,
                officialSite = officialSite,
                premiered = premiered,
                summary = summary,
            )
        })?.asFlow()?.mapToList() ?: flowOf(emptyList<Show>())
    }

    private suspend fun fetchAndStorePeople()  {
        logger.d { "fetchAndStorePeople" }
        val result = tvMazeApi.fetchShows("girls")

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        tvProgramSearcherQueries?.deleteAll()
        result.forEach {
            tvProgramSearcherQueries?.insertItem(
                it.show.id,
                it.show.image,
                it.show.language,
                it.show.name,
                it.show.officialSite,
                it.show.premiered,
                it.show.genres.joinToString(),
                it.show.summary
                )
        }
    }

    // Used by web client atm
    suspend fun fetchPeople() = tvMazeApi.fetchShows("girls")

    fun getPersonBio(personName: String): String {
        return personBios[personName] ?: ""
    }

    fun getPersonImage(personName: String): String {
        return personImages[personName] ?: ""
    }

    // called from Kotlin/Native clients
    fun startObservingPeopleUpdates(success: (List<Show>) -> Unit) {
        logger.d { "startObservingTvUpdates" }
        peopleJob = coroutineScope.launch {
            fetchPeopleAsFlow().collect {
                success(it)
            }
        }
    }

    fun stopObservingPeopleUpdates() {
        logger.d { "stopObservingPeopleUpdates, peopleJob = $peopleJob" }
        peopleJob?.cancel()
    }


//    fun pollISSPosition(): Flow<IssPosition> = flow {
//        while (true) {
//            val position = tvMazeApi.fetchISSPosition().iss_position
//            emit(position)
//            logger.d("PeopleInSpaceRepository") { position.toString() }
//            delay(POLL_INTERVAL)
//        }
//    }



    val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }


    companion object {
        private const val POLL_INTERVAL = 10000L
    }
}


class KotlinNativeFlowWrapper<T>(private val flow: Flow<T>) {
    fun subscribe(
        scope: CoroutineScope,
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ) = flow
        .onEach { onEach(it) }
        .catch { onThrow(it) }
        .onCompletion { onComplete() }
        .launchIn(scope)
}

