package com.mateuszkrawczuk.tvprogramsearcher.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Kermit
import com.mateuszkrawczuk.TvProgramSearcher.common.remote.Show
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.TvProgramSearcherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TvProgramSearcherViewModel(
    private val tvProgramSearcherRepository: TvProgramSearcherRepository,
    private val logger: Kermit
) : ViewModel() {
    val query = mutableStateOf("")
    private val coroutineScope: CoroutineScope = MainScope()

    fun onQueryChanged(query: String){
        this.query.value = query
        if(query.length >2 ) {
            coroutineScope.launch {
                tvProgramSearcherRepository.fetchAndStoreShows(query)
            }
        }
    }

    val showsInMaze = tvProgramSearcherRepository.fetchPeopleAsFlow()
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun getPersonBio(personName: String): String {
        return tvProgramSearcherRepository.getPersonBio(personName)
    }

    fun getShowImage(personName: String): String {
        return tvProgramSearcherRepository.getshowImage(personName)
    }

    fun getShow(showName: String): Show? {
        return showsInMaze.value.find { it.name == showName}
    }
}