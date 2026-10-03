package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.BottomNavTab
import com.example.ui.navigation.Screen
import com.example.ui.player.VideoPlayerView
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoviesScreen
import com.example.ui.screens.MySpaceScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SeriesScreen
import com.example.ui.screens.UploadStudioScreen
import com.example.ui.theme.HotstarBlue
import com.example.ui.theme.HotstarGold
import com.example.ui.theme.HotstarNavyDark
import com.example.ui.theme.HotstarSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.MediaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                JioHotstarApp()
            }
        }
    }
}

@Composable
fun JioHotstarApp() {
    val navController = rememberNavController()
    val mediaViewModel: MediaViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom navigation bar in player and detail views
    val isBottomBarVisible = currentRoute in listOf(
        Screen.Home.route,
        Screen.Movies.route,
        Screen.Series.route,
        Screen.Studio.route,
        Screen.MySpace.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = HotstarSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("main_bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        Triple(BottomNavTab.HOME, Icons.Default.Home, "nav_home"),
                        Triple(BottomNavTab.MOVIES, Icons.Default.Movie, "nav_movies"),
                        Triple(BottomNavTab.SERIES, Icons.Default.Tv, "nav_series"),
                        Triple(BottomNavTab.STUDIO, Icons.Default.CloudUpload, "nav_studio"),
                        Triple(BottomNavTab.MY_SPACE, Icons.Default.Person, "nav_my_space")
                    )

                    tabs.forEach { (tab, icon, testTag) ->
                        val isSelected = currentRoute == tab.route
                        val isStudio = tab == BottomNavTab.STUDIO

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(if (isStudio) 26.dp else 22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = if (isStudio) HotstarGold else HotstarBlue,
                                selectedTextColor = if (isStudio) HotstarGold else HotstarBlue,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag(testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isBottomBarVisible) innerPadding.calculateBottomPadding() else 0.dp)
                .background(HotstarNavyDark)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = mediaViewModel,
                        onNavigateToDetail = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        },
                        onNavigateToPlayer = { mediaId, episodeId ->
                            navController.navigate(Screen.Player.createRoute(mediaId, episodeId))
                        },
                        onNavigateToSearch = {
                            navController.navigate(Screen.Search.route)
                        }
                    )
                }

                composable(Screen.Movies.route) {
                    MoviesScreen(
                        viewModel = mediaViewModel,
                        onNavigateToDetail = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                    )
                }

                composable(Screen.Series.route) {
                    SeriesScreen(
                        viewModel = mediaViewModel,
                        onNavigateToDetail = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                    )
                }

                composable(Screen.Studio.route) {
                    UploadStudioScreen(
                        viewModel = mediaViewModel,
                        onNavigateToMedia = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                    )
                }

                composable(Screen.MySpace.route) {
                    MySpaceScreen(
                        viewModel = mediaViewModel,
                        onNavigateToDetail = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        viewModel = mediaViewModel,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToDetail = { id ->
                            navController.navigate(Screen.Detail.createRoute(id))
                        }
                    )
                }

                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("mediaId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val mediaId = backStackEntry.arguments?.getLong("mediaId") ?: 0L
                    DetailScreen(
                        mediaId = mediaId,
                        viewModel = mediaViewModel,
                        onBackClick = { navController.popBackStack() },
                        onPlayClick = { id, epId ->
                            navController.navigate(Screen.Player.createRoute(id, epId))
                        },
                        onNavigateToDetail = { newId ->
                            navController.navigate(Screen.Detail.createRoute(newId))
                        }
                    )
                }

                composable(
                    route = Screen.Player.route,
                    arguments = listOf(
                        navArgument("mediaId") { type = NavType.LongType },
                        navArgument("episodeId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val mediaId = backStackEntry.arguments?.getLong("mediaId") ?: 0L
                    val epIdString = backStackEntry.arguments?.getString("episodeId")
                    val currentEpId = epIdString?.toLongOrNull()

                    val allMedia by mediaViewModel.allMedia.collectAsStateWithLifecycle()
                    val mediaItem = allMedia.find { it.id == mediaId }
                    val episodes by mediaViewModel.getEpisodesForSeries(mediaId).collectAsStateWithLifecycle()

                    val activeEpisode = episodes.find { it.id == currentEpId } ?: episodes.firstOrNull()

                    val videoUri = if (activeEpisode != null && activeEpisode.videoUri.isNotBlank()) {
                        activeEpisode.videoUri
                    } else {
                        mediaItem?.videoUri.orEmpty()
                    }

                    val playerTitle = mediaItem?.title ?: "Playing"
                    val playerSubtitle = if (activeEpisode != null) {
                        "S${activeEpisode.seasonNumber} E${activeEpisode.episodeNumber} • ${activeEpisode.title}"
                    } else {
                        "${mediaItem?.genre ?: ""} • 1080p Atmos"
                    }

                    val currentEpIndex = episodes.indexOfFirst { it.id == activeEpisode?.id }
                    val hasPrevEp = currentEpIndex > 0
                    val hasNextEp = currentEpIndex >= 0 && currentEpIndex < episodes.size - 1

                    VideoPlayerView(
                        videoUriString = videoUri,
                        title = playerTitle,
                        subtitle = playerSubtitle,
                        onBackClick = { navController.popBackStack() },
                        onProgressUpdate = { posMs, totalMs ->
                            mediaViewModel.saveWatchProgress(mediaId, activeEpisode?.id, posMs, totalMs)
                        },
                        hasPreviousEpisode = hasPrevEp,
                        hasNextEpisode = hasNextEp,
                        onPreviousEpisode = {
                            if (hasPrevEp) {
                                val prevEp = episodes[currentEpIndex - 1]
                                navController.navigate(Screen.Player.createRoute(mediaId, prevEp.id)) {
                                    popUpTo(Screen.Player.route) { inclusive = true }
                                }
                            }
                        },
                        onNextEpisode = {
                            if (hasNextEp) {
                                val nextEp = episodes[currentEpIndex + 1]
                                navController.navigate(Screen.Player.createRoute(mediaId, nextEp.id)) {
                                    popUpTo(Screen.Player.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
