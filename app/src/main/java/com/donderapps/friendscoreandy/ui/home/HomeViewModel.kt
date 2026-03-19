package com.donderapps.friendscoreandy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donderapps.friendscoreandy.domain.model.FriendWithScore
import com.donderapps.friendscoreandy.domain.model.SortConfig
import com.donderapps.friendscoreandy.domain.model.SortMode
import com.donderapps.friendscoreandy.domain.model.Trophy
import com.donderapps.friendscoreandy.domain.usecase.GetFriendsWithScoresUseCase
import com.donderapps.friendscoreandy.domain.usecase.GetTrophyRankingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFriendsWithScoresUseCase: GetFriendsWithScoresUseCase,
    private val getTrophyRankingsUseCase: GetTrophyRankingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFriends()
    }

    private fun loadFriends() {
        viewModelScope.launch {
            getFriendsWithScoresUseCase.execute()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { friends ->
                    val trophies = getTrophyRankingsUseCase.execute(friends)
                    val friendsWithTrophies = friends.map { friend ->
                        friend.copy(trophy = trophies[friend.friend.id])
                    }
                    val sortConfig = _uiState.value.sortConfig
                    val sorted = applySorting(friendsWithTrophies, sortConfig)
                    val globalScore = computeGlobalScore(friendsWithTrophies, sortConfig.mode)

                    _uiState.update {
                        it.copy(
                            friends = sorted,
                            globalScore = globalScore,
                            isFirstLaunch = friends.isEmpty(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun changeSortMode(mode: SortMode) {
        _uiState.update { state ->
            val newConfig = state.sortConfig.copy(mode = mode)
            val sorted = applySorting(state.friends, newConfig)
            val globalScore = computeGlobalScore(state.friends, newConfig.mode)
            state.copy(friends = sorted, sortConfig = newConfig, globalScore = globalScore)
        }
    }

    fun toggleSortDirection() {
        _uiState.update { state ->
            val newConfig = state.sortConfig.copy(ascending = !state.sortConfig.ascending)
            val sorted = applySorting(state.friends, newConfig)
            state.copy(friends = sorted, sortConfig = newConfig)
        }
    }

    private fun applySorting(
        friends: List<FriendWithScore>,
        config: SortConfig
    ): List<FriendWithScore> {
        val comparator: Comparator<FriendWithScore> = when (config.mode) {
            SortMode.ALL_TIME_SCORE -> compareBy<FriendWithScore> { it.score.allTime ?: -1f }
            SortMode.WEEKLY_SCORE -> compareBy<FriendWithScore> { it.score.weekly ?: -1f }
            SortMode.DATE_ADDED -> compareBy<FriendWithScore> { it.friend.createdAt }
            SortMode.MOST_RECENT_EVENT -> compareBy<FriendWithScore> { it.lastEvent?.createdAt ?: 0L }
        }
        return if (config.ascending) friends.sortedWith(comparator) else friends.sortedWith(comparator.reversed())
    }

    private fun computeGlobalScore(friends: List<FriendWithScore>, mode: SortMode): Float? {
        if (friends.isEmpty()) return null
        val scores = when (mode) {
            SortMode.WEEKLY_SCORE -> friends.mapNotNull { it.score.weekly }
            else -> friends.mapNotNull { it.score.allTime }
        }
        return if (scores.isEmpty()) null else scores.average().toFloat()
    }
}
