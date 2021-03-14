package com.mateuszkrawczuk.tvprogramsearcher.common.repository

import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.tvprogramsearcher.common.model.personBios
import com.mateuszkrawczuk.tvprogramsearcher.common.model.personImages
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.Assignment
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.IssPosition
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.TvMazeApi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
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

    fun fetchPeopleAsFlow(): Flow<List<Assignment>> {
        // the main reason we need to do this check is that sqldelight isn't currently
        // setup for javascript client
        return tvProgramSearcherQueries?.selectAll(mapper = { name, craft ->
            Assignment(name = name, craft = craft)
        })?.asFlow()?.mapToList() ?: flowOf(emptyList<Assignment>())
    }

    private suspend fun fetchAndStorePeople()  {
        logger.d { "fetchAndStorePeople" }
        val result = tvMazeApi.fetchPeople()

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        tvProgramSearcherQueries?.deleteAll()
        result.people.forEach {
            tvProgramSearcherQueries?.insertItem(it.name, it.craft)
        }
    }

    // Used by web client atm
    suspend fun fetchPeople() = tvMazeApi.fetchPeople().people

    fun getPersonBio(personName: String): String {
        return personBios[personName] ?: ""
    }

    fun getPersonImage(personName: String): String {
        return personImages[personName] ?: ""
    }

    // called from Kotlin/Native clients
    fun startObservingPeopleUpdates(success: (List<Assignment>) -> Unit) {
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


    fun pollISSPosition(): Flow<IssPosition> = flow {
        while (true) {
            val position = tvMazeApi.fetchISSPosition().iss_position
            emit(position)
            logger.d("PeopleInSpaceRepository") { position.toString() }
            delay(POLL_INTERVAL)
        }
    }



    val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

    fun iosPollISSPosition() = KotlinNativeFlowWrapper<IssPosition>(pollISSPosition())


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

