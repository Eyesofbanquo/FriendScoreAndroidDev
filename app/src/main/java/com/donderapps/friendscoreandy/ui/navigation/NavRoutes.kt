package com.donderapps.friendscoreandy.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable data object Home : NavRoute
    @Serializable data class FriendDetail(val friendId: String) : NavRoute
    @Serializable data object AddFriend : NavRoute
    @Serializable data object Search : NavRoute
    @Serializable data object Settings : NavRoute
}
