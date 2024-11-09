package com.tifd.projectcomposed

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tifd.projectcomposed.data.model.local.TaskRepository
import com.tifd.projectcomposed.navigation.NavigationItem
import com.tifd.projectcomposed.navigation.Screen
import com.tifd.projectcomposed.screen.CameraScreen
import com.tifd.projectcomposed.screen.ProfileScreen
import com.tifd.projectcomposed.screen.ScheduleScreen
import com.tifd.projectcomposed.screen.TaskScreen
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme
import com.tifd.projectcomposed.viewmodel.MainViewModel
import com.tifd.projectcomposed.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent(application)
        }
    }
}

@Composable
fun MainActivityContent(
    application: Application,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(application))

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost (
            navController = navController,
            startDestination = Screen.Schedule.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Schedule.route) {
                ScheduleScreen()
            }
            composable(Screen.Task.route) {
                TaskScreen(navController = navController, viewModel = mainViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = mainViewModel)
            }
            composable(Screen.Camera.route) {
                CameraScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar (
        modifier = modifier
    ) {
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.schedule),
                icon = Icons.Default.Home,
                screen = Screen.Schedule,
            ),
            NavigationItem(
                title = stringResource(R.string.task),
                icon = Icons.Default.Favorite,
                screen = Screen.Task,
            ),
            NavigationItem(
                title = stringResource(R.string.profile),
                icon = Icons.Default.Person,
                screen = Screen.Profile,
            ),
        )
        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title
                    )
                },
                label = {
                    Text(it.title)
                },
                selected = false,
                onClick = {
                    navController.navigate(it.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}