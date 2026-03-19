package com.donderapps.friendscoreandy.data.repository

import com.donderapps.friendscoreandy.data.local.db.dao.EventDao
import com.donderapps.friendscoreandy.data.local.db.entity.EventEntity
import com.donderapps.friendscoreandy.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {

    fun getByFriendId(friendId: String): Flow<List<Event>> {
        return eventDao.getByFriendId(friendId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getDiaryEvents(): Flow<List<Event>> {
        return eventDao.getDiaryEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getLatestByFriendId(friendId: String): Flow<Event?> {
        return eventDao.getLatestByFriendId(friendId).map { it?.toDomain() }
    }

    fun getById(eventId: String): Flow<Event?> {
        return eventDao.getById(eventId).map { it?.toDomain() }
    }

    suspend fun insert(friendId: String?, rating: Int, description: String?): String {
        val id = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        eventDao.insert(
            EventEntity(
                id = id,
                friendId = friendId,
                rating = rating,
                description = description,
                createdAt = now,
                updatedAt = now
            )
        )
        return id
    }

    suspend fun update(event: Event) {
        eventDao.update(
            EventEntity(
                id = event.id,
                friendId = event.friendId,
                rating = event.rating,
                description = event.description,
                createdAt = event.createdAt,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun delete(eventId: String) {
        eventDao.delete(eventId)
    }
}

private fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        friendId = friendId,
        rating = rating,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
