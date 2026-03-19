package com.donderapps.friendscoreandy.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.donderapps.friendscoreandy.data.local.db.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun delete(eventId: String)

    @Query("SELECT * FROM events WHERE friendId = :friendId ORDER BY createdAt DESC")
    fun getByFriendId(friendId: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE friendId IS NULL ORDER BY createdAt DESC")
    fun getDiaryEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE friendId = :friendId AND createdAt >= :start AND createdAt < :end ORDER BY createdAt DESC")
    fun getByFriendIdInDateRange(friendId: String, start: Long, end: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE friendId = :friendId ORDER BY createdAt DESC LIMIT 1")
    fun getLatestByFriendId(friendId: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getById(eventId: String): Flow<EventEntity?>
}
