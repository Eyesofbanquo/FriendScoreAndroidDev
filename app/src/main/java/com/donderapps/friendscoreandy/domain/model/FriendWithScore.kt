package com.donderapps.friendscoreandy.domain.model

data class FriendWithScore(
    val friend: Friend,
    val score: Score,
    val lastEvent: Event?,
    val trophy: Trophy?
)
