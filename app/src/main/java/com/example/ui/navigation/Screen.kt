package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Movies : Screen("movies")
    object Series : Screen("series")
    object Studio : Screen("studio")
    object MySpace : Screen("my_space")
    object Search : Screen("search")
    
    object Detail : Screen("detail/{mediaId}") {
        fun createRoute(mediaId: Long) = "detail/$mediaId"
    }

    object Player : Screen("player/{mediaId}?episodeId={episodeId}") {
        fun createRoute(mediaId: Long, episodeId: Long? = null): String {
            return if (episodeId != null) {
                "player/$mediaId?episodeId=$episodeId"
            } else {
                "player/$mediaId"
            }
        }
    }
}

enum class BottomNavTab(
    val title: String,
    val route: String,
    val iconName: String
) {
    HOME("Home", Screen.Home.route, "home"),
    MOVIES("Movies", Screen.Movies.route, "movie"),
    SERIES("Web Series", Screen.Series.route, "tv"),
    STUDIO("Upload", Screen.Studio.route, "upload"),
    MY_SPACE("My Space", Screen.MySpace.route, "person")
}
