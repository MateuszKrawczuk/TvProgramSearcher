package com.mateuszkrawczuk.tvprogramsearcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.mateuszkrawczuk.tvprogramsearcher.BuildConfig
import org.koin.androidx.compose.getViewModel
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // needed for osmandroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        setContent {
            MainLayout()
        }
    }
}

sealed class Screen(val title: String) {
    object ShowList : Screen("ShowList")
    object ShowDetails : Screen("ShowDetails")
}

data class BottomNavigationitem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

@ExperimentalComposeUiApi
@Composable
fun MainLayout() {
    val navController = rememberNavController()

    val tvProgramSearcherViewModel = getViewModel<TvProgramSearcherViewModel>()

    Scaffold {
        NavHost(navController, startDestination = Screen.ShowList.title) {
            composable(Screen.ShowList.title) {
                ShowListScreen(
                    showSelected = {
                        navController.navigate(Screen.ShowDetails.title + "/${it.name}")
                    }
                )
            }
            composable(Screen.ShowDetails.title + "/{show}") { backStackEntry ->
                ShowDetailsScreen(
                    backStackEntry.arguments?.get("show") as String,
                    popBack = { navController.popBackStack() })
            }
        }
    }
}