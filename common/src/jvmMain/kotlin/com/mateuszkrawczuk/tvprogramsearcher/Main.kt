package com.surrus

import com.surrus.common.di.initKoin
import com.surrus.common.remote.TvMazeApi
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val koin = initKoin(enableNetworkLogs = true).koin
        val api = koin.get<TvMazeApi>()
        println(api.fetchPeople())
    }
}
