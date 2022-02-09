package com.mateuszkrawczuk.tvprogramsearcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel

@Composable
fun ShowDetailsScreen(showName: String, popBack: () -> Unit) {
    val tvMazeViewModel: TvProgramSearcherViewModel = getViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(showName) },
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
            val show = tvMazeViewModel.getShow(showName)
            show?.let {
                Text(show.name, style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.size(12.dp))

                val imageUrl = it.image?.medium ?: ""
                if (imageUrl.isNotEmpty()) {
                    Image(painter = rememberImagePainter(imageUrl), modifier = Modifier.size(240.dp), contentDescription = show.name)
                }
                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}
