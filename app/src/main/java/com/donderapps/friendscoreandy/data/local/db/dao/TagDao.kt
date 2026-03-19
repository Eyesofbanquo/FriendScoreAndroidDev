package com.donderapps.friendscoreandy.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.donderapps.friendscoreandy.data.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity)

    @Query("DELETE FROM tags WHERE id = :tagId")
    suspend fun delete(tagId: String)

    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAll(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE id IN (:tagIds)")
    fun getByIds(tagIds: List<String>): Flow<List<TagEntity>>
}
