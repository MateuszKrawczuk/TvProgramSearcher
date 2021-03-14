package com.mateuszkrawczuk.tvprogramsearcher.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.TvProgramSearcher.common.model.Show
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.TvProgramSearcherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TvProgramSearcherViewModel(
    private val tvProgramSearcherRepository: TvProgramSearcherRepository,
    private val logger: Kermit
) : ViewModel() {
    val query = mutableStateOf("")
    private val coroutineScope: CoroutineScope = MainScope()

    fun onQueryChanged(query: String){
        this.query.value = query
        if(query.length >2 ) {
            tvProgramSearcherRepository.updateSearchList(query)
        }
    }

    val showsInMaze = tvProgramSearcherRepository.fetchShowsAsFlow()
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun getShow(showName: String): Show? {
        return showsInMaze.value.find { it.name == showName}
    }
}