package com.donderapps.friendscoreandy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.donderapps.friendscoreandy.data.local.db.dao.EventDao
import com.donderapps.friendscoreandy.data.local.db.dao.FriendDao
import com.donderapps.friendscoreandy.data.local.db.dao.TagDao
import com.donderapps.friendscoreandy.data.local.db.entity.EventEntity
import com.donderapps.friendscoreandy.data.local.db.entity.FriendEntity
import com.donderapps.friendscoreandy.data.local.db.entity.FriendTagCrossRef
import com.donderapps.friendscoreandy.data.local.db.entity.TagEntity

@Database(
    entities = [
        FriendEntity::class,
        TagEntity::class,
        FriendTagCrossRef::class,
        EventEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FriendScoreDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao
    abstract fun tagDao(): TagDao
    abstract fun eventDao(): EventDao
}
