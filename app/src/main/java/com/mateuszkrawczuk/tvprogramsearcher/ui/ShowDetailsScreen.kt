package com.mateuszkrawczuk.tvprogramsearcher.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import org.koin.androidx.compose.getViewModel

@Composable
fun ShowDetailsScreen(personName: String, popBack: () -> Unit) {
    val tvMazeViewModel = getViewModel<TvProgramSearcherViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(personName) },
                navigationIcon = {
                    IconButton(onClick = { popBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val person = tvMazeViewModel.getShow(personName)
            person?.let {
                Text(person.name, style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.size(12.dp))

                val imageUrl = it.image?.medium ?: ""
                if (imageUrl.isNotEmpty()) {
                    CoilImage(data = imageUrl, modifier = Modifier.size(240.dp), contentDescription = person.name)
                }
                Spacer(modifier = Modifier.size(24.dp))

                val bio = tvMazeViewModel.getPersonBio(person.name)
                Text(bio, style = MaterialTheme.typography.body1)
            }
        }
    }
}