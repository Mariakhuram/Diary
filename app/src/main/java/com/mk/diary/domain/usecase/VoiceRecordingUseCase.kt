package com.mk.diary.domain.usecase

import com.mk.diary.domain.repository.VoiceRecordingRepository
import com.mk.diary.helpers.VoiceRecorderHelper
import com.mk.diary.helpers.voicerecordingplaying.RecordingModelClass
import javax.inject.Inject

class VoiceRecordingUseCase @Inject constructor(private val repository: VoiceRecordingRepository) {

    suspend fun startVoiceRecording(filePath: String): VoiceRecorderHelper<RecordingModelClass> {
        return repository.startVoiceRecording(filePath)
    }

    suspend fun stopVoiceRecording(): VoiceRecorderHelper<RecordingModelClass> {
        return repository.stopVoiceRecording()
    }
}