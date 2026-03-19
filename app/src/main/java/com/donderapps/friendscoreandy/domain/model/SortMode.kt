package com.donderapps.friendscoreandy.domain.model

enum class SortMode {
    ALL_TIME_SCORE,
    WEEKLY_SCORE,
    DATE_ADDED,
    MOST_RECENT_EVENT
}

data class SortConfig(
    val mode: SortMode,
    val ascending: Boolean = false
)
