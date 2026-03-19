package com.donderapps.friendscoreandy.data.repository

import com.donderapps.friendscoreandy.data.local.db.dao.FriendDao
import com.donderapps.friendscoreandy.data.local.db.entity.FriendEntity
import com.donderapps.friendscoreandy.domain.model.Friend
import com.donderapps.friendscoreandy.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepository @Inject constructor(
    private val friendDao: FriendDao
) {

    fun getAll(): Flow<List<Friend>> {
        return friendDao.getAll().map { entities ->
            entities.map { it.toDomain(emptyList()) }
        }
    }

    fun getById(friendId: String): Flow<Friend?> {
        return combine(
            friendDao.getById(friendId),
            friendDao.getTagsForFriend(friendId)
        ) { entity, tagEntities ->
            entity?.toDomain(tagEntities.map { Tag(it.id, it.name) })
        }
    }

    fun getTagsForFriend(friendId: String): Flow<List<Tag>> {
        return friendDao.getTagsForFriend(friendId).map { entities ->
            entities.map { Tag(it.id, it.name) }
        }
    }

    fun getAllTimeScore(friendId: String): Flow<Float?> {
        return friendDao.getAllTimeScore(friendId)
    }

    fun getWeeklyScore(friendId: String, weekStart: Long, weekEnd: Long): Flow<Float?> {
        return friendDao.getWeeklyScore(friendId, weekStart, weekEnd)
    }

    suspend fun insert(name: String, photoUri: String?, description: String?, tagIds: List<String>): String {
        val id = UUID.randomUUID().toString()
        friendDao.insert(
            FriendEntity(
                id = id,
                name = name,
                photoUri = photoUri,
                description = description,
                createdAt = System.currentTimeMillis()
            )
        )
        friendDao.setTagsForFriend(id, tagIds)
        return id
    }

    suspend fun update(friend: Friend) {
        friendDao.update(
            FriendEntity(
                id = friend.id,
                name = friend.name,
                photoUri = friend.photoUri,
                description = friend.description,
                createdAt = friend.createdAt
            )
        )
        friendDao.setTagsForFriend(friend.id, friend.tags.map { it.id })
    }

    suspend fun delete(friendId: String) {
        friendDao.delete(friendId)
    }
}

private fun FriendEntity.toDomain(tags: List<Tag>): Friend {
    return Friend(
        id = id,
        name = name,
        photoUri = photoUri,
        description = description,
        createdAt = createdAt,
        tags = tags
    )
}
