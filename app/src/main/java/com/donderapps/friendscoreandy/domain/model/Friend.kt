package com.donderapps.friendscoreandy.domain.model

data class Friend(
    val id: String,
    val name: String,
    val photoUri: String?,
    val description: String?,
    val createdAt: Long,
    val tags: List<Tag>
)
