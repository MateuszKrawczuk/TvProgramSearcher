package com.mateuszkrawczuk.tvprogramsearcher

import com.mateuszkrawczuk.tvprogramsearcher.common.di.initKoin
import com.mateuszkrawczuk.tvprogramsearcher.common.remote.TvMazeApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class TvMazeTest {
    @Test
    fun testGetShows() = runBlocking {
        val koin = initKoin(enableNetworkLogs = true).koin
        val tvMazeApi = koin.get<TvMazeApi>()
        val result = tvMazeApi.fetchShows("Legend")
        println(result)
        assertTrue(result.isNotEmpty())
    }
}
