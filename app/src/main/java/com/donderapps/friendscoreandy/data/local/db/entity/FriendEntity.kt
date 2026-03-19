package com.donderapps.friendscoreandy.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey val id: String,
    val name: String,
    val photoUri: String?,
    val description: String?,
    val createdAt: Long
)
