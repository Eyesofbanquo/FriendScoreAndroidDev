package com.donderapps.friendscoreandy.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.donderapps.friendscoreandy.data.local.db.entity.FriendEntity
import com.donderapps.friendscoreandy.data.local.db.entity.FriendTagCrossRef
import com.donderapps.friendscoreandy.data.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friend: FriendEntity)

    @Update
    suspend fun update(friend: FriendEntity)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun delete(friendId: String)

    @Query("SELECT * FROM friends WHERE id = :friendId")
    fun getById(friendId: String): Flow<FriendEntity?>

    @Query("SELECT * FROM friends")
    fun getAll(): Flow<List<FriendEntity>>

    @Query("SELECT tags.* FROM tags INNER JOIN friend_tag ON tags.id = friend_tag.tagId WHERE friend_tag.friendId = :friendId")
    fun getTagsForFriend(friendId: String): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriendTagCrossRef(crossRef: FriendTagCrossRef)

    @Query("DELETE FROM friend_tag WHERE friendId = :friendId")
    suspend fun clearTagsForFriend(friendId: String)

    @Transaction
    suspend fun setTagsForFriend(friendId: String, tagIds: List<String>) {
        clearTagsForFriend(friendId)
        tagIds.forEach { tagId ->
            insertFriendTagCrossRef(FriendTagCrossRef(friendId, tagId))
        }
    }

    @Query("SELECT AVG(rating) FROM events WHERE friendId = :friendId")
    fun getAllTimeScore(friendId: String): Flow<Float?>

    @Query("SELECT AVG(rating) FROM events WHERE friendId = :friendId AND createdAt >= :weekStart AND createdAt < :weekEnd")
    fun getWeeklyScore(friendId: String, weekStart: Long, weekEnd: Long): Flow<Float?>
}
