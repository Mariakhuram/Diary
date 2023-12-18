package com.mk.diary.domain.repository

import com.mk.diary.helpers.VoiceRecorderHelper
import com.mk.diary.helpers.voicerecordingplaying.RecordingModelClass

interface VoiceRecordingRepository {
    suspend fun startVoiceRecording(filePath:String): VoiceRecorderHelper<RecordingModelClass>
    suspend fun stopVoiceRecording():VoiceRecorderHelper<RecordingModelClass>
}