package com.donderapps.friendscoreandy.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home,
        modifier = modifier
    ) {
        composable<NavRoute.Home> {
            PlaceholderScreen("Home")
        }

        composable<NavRoute.FriendDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.FriendDetail>()
            PlaceholderScreen("Friend Detail: ${route.friendId}")
        }

        composable<NavRoute.AddFriend> {
            PlaceholderScreen("Add Friend")
        }

        composable<NavRoute.Search> {
            PlaceholderScreen("Search")
        }

        composable<NavRoute.Settings> {
            PlaceholderScreen("Settings")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}
