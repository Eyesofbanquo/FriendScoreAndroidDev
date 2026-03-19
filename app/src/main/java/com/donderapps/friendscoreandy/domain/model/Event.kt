package com.donderapps.friendscoreandy.domain.model

data class Event(
    val id: String,
    val friendId: String?,
    val rating: Int,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long
)
