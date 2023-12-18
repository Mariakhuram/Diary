package com.mk.diary.di.module

import android.app.Application
import android.content.Context
import com.mk.diary.data.entity.NoteViewDao
import com.mk.diary.data.repositoryimp.NoteViewRepositoryImp
import com.mk.diary.data.repositoryimp.VoicePlayingRepositoryImp
import com.mk.diary.data.repositoryimp.VoiceRecordingRepositoryImp
import com.mk.diary.domain.repository.NoteViewRepository
import com.mk.diary.domain.repository.VoicePlayingRepository
import com.mk.diary.domain.repository.VoiceRecordingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplicationContext(application: Application): Context = application.applicationContext


    @Provides
    fun saveNoteViewRep(dao: NoteViewDao):NoteViewRepository= NoteViewRepositoryImp(dao)

    @Provides
    fun voiceRecordingRep():VoiceRecordingRepository= VoiceRecordingRepositoryImp()

    @Provides
    fun voicePlayingRep():VoicePlayingRepository= VoicePlayingRepositoryImp()

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}