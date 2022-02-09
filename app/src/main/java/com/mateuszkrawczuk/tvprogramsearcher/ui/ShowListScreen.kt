package com.mateuszkrawczuk.tvprogramsearcher.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mateuszkrawczuk.TvProgramSearcher.common.model.Show
import coil.compose.rememberImagePainter
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
@Composable
fun ShowListScreen(showSelected : (show : Show) -> Unit) {
    val tvMazeViewModel : TvProgramSearcherViewModel = getViewModel()
    val showState = tvMazeViewModel.showsInMaze.collectAsState()
    val query = tvMazeViewModel.query.value
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                color = MaterialTheme.colors.primary,
                elevation = 8.dp,
            ){
                Row(modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = query,
                        onValueChange = {
                            Log.i("","")
                            tvMazeViewModel.onQueryChanged(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(8.dp),
                        label = {
                            Text(text = "Search")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(

                            onDone = {keyboardController?.hideSoftwareKeyboard()}),
                        leadingIcon = {
                            Icon(Icons.Filled.Search,"Search")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
//                        colors = MaterialTheme.colors
                    )
                }
            }
        }) {
        Column {
            LazyColumn {
                items(showState.value) { show ->
                    val showImageUrl = show.image?.medium ?: ""
                    showView(showImageUrl, show, showSelected)
                }
            }
        }
    }
}


@Composable
fun showView(showImageUrl: String, show: Show, showSelected : (show : Show) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { showSelected(show) })
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        if (showImageUrl.isNotEmpty()) {
            Image(painter = rememberImagePainter(showImageUrl), modifier = Modifier.size(60.dp), contentDescription = show.name)
        } else {
            Spacer(modifier = Modifier.size(60.dp))
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(text = show.name, style = TextStyle(fontSize = 20.sp))
            Text(text = show.genres.joinToString(), style = TextStyle(color = Color.DarkGray, fontSize = 14.sp))
        }
    }
}

