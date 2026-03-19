package com.donderapps.friendscoreandy.di

import android.content.Context
import androidx.room.Room
import com.donderapps.friendscoreandy.data.local.db.FriendScoreDatabase
import com.donderapps.friendscoreandy.data.local.db.dao.EventDao
import com.donderapps.friendscoreandy.data.local.db.dao.FriendDao
import com.donderapps.friendscoreandy.data.local.db.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FriendScoreDatabase {
        return Room.databaseBuilder(
            context,
            FriendScoreDatabase::class.java,
            "friendscore.db"
        ).build()
    }

    @Provides
    fun provideFriendDao(database: FriendScoreDatabase): FriendDao = database.friendDao()

    @Provides
    fun provideTagDao(database: FriendScoreDatabase): TagDao = database.tagDao()

    @Provides
    fun provideEventDao(database: FriendScoreDatabase): EventDao = database.eventDao()
}
