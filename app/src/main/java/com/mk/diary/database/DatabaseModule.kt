package com.mk.diary.database

import android.content.Context
import androidx.room.Room
import com.mk.diary.data.entity.NoteViewDao
import com.mk.diary.database.AppDatabase.Callback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): NoteViewDao {
        return appDatabase.recordDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: Callback
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "data"
        ).fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    }
}