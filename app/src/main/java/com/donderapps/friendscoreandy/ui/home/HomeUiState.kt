package com.donderapps.friendscoreandy.ui.home

import com.donderapps.friendscoreandy.domain.model.FriendWithScore
import com.donderapps.friendscoreandy.domain.model.SortConfig
import com.donderapps.friendscoreandy.domain.model.SortMode

data class HomeUiState(
    val friends: List<FriendWithScore> = emptyList(),
    val globalScore: Float? = null,
    val sortConfig: SortConfig = SortConfig(SortMode.ALL_TIME_SCORE, ascending = false),
    val isFirstLaunch: Boolean = true,
    val isLoading: Boolean = true,
    val error: String? = null
)
