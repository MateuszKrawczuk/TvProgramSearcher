package com.mateuszkrawczuk.tvprogramsearcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*
import com.mateuszkrawczuk.tvprogramsearcher.BuildConfig
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

sealed class Screen(val title: String) {
    object PersonList : Screen("PersonList")
    object PersonDetails : Screen("PersonDetails")
    object ISSPositionScreen : Screen("ISSPosition")
}

data class BottomNavigationitem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

val bottomNavigationItems = listOf(
    BottomNavigationitem(
        Screen.PersonList.title,
        Icons.Default.Person,
        "People"
    ),
)

@Composable
fun MainLayout() {
    val navController = rememberNavController()

    val tvProgramSearcherViewModel = getViewModel<TvProgramSearcherViewModel>()

    Scaffold(

        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

                bottomNavigationItems.forEach { bottomNavigationitem ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                bottomNavigationitem.icon,
                                contentDescription = bottomNavigationitem.iconContentDescription
                            )
                        },
                        selected = currentRoute == bottomNavigationitem.route,
                        onClick = {
                            navController.navigate(bottomNavigationitem.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = Screen.PersonList.title) {
            composable(Screen.PersonList.title) {
                ShowListScreen(
                    showSelected = {
                        navController.navigate(Screen.PersonDetails.title + "/${it.name}")
                    }
                )
            }
            composable(Screen.PersonDetails.title + "/{person}") { backStackEntry ->
                ShowDetailsScreen(
                    backStackEntry.arguments?.get("person") as String,
                    popBack = { navController.popBackStack() })
            }
        }
    }



}