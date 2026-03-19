package com.donderapps.friendscoreandy.data.repository

import com.donderapps.friendscoreandy.data.local.db.dao.TagDao
import com.donderapps.friendscoreandy.data.local.db.entity.TagEntity
import com.donderapps.friendscoreandy.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val tagDao: TagDao
) {

    fun getAll(): Flow<List<Tag>> {
        return tagDao.getAll().map { entities ->
            entities.map { Tag(it.id, it.name) }
        }
    }

    suspend fun insert(name: String): String {
        val id = UUID.randomUUID().toString()
        tagDao.insert(TagEntity(id = id, name = name))
        return id
    }

    suspend fun delete(tagId: String) {
        tagDao.delete(tagId)
    }
}
