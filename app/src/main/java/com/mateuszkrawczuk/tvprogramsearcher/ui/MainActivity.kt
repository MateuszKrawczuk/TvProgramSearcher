package com.mateuszkrawczuk.tvprogramsearcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mateuszkrawczuk.tvprogramsearcher.BuildConfig
import com.mateuszkrawczuk.tvprogramsearcher.common.repository.getLogger
import org.koin.androidx.compose.getViewModel
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // needed for osmandroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        setContent {

            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {
    val tvProgramSearcherViewModel = getViewModel<TvProgramSearcherViewModel>()
    val query = tvProgramSearcherViewModel.query.value
    TopAppBar(
        title = { TextField(value = query,
                    onValueChange = {
                        newValue -> getLogger().i("newValue","")
                    }) },
        backgroundColor = Color.White

    )


}