package com.donderapps.friendscoreandy.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = FriendEntity::class,
            parentColumns = ["id"],
            childColumns = ["friendId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["friendId"])]
)
data class EventEntity(
    @PrimaryKey val id: String,
    val friendId: String?,
    val rating: Int,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long
)
