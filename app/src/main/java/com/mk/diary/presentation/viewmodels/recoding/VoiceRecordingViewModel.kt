package com.mk.diary.presentation.viewmodels.recoding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.diary.domain.usecase.VoiceRecordingUseCase
import com.mk.diary.helpers.VoiceRecorderHelper
import com.mk.diary.helpers.voicerecordingplaying.RecordingModelClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceRecordingViewModel @Inject constructor(
    private val voiceRecordingUseCase: VoiceRecordingUseCase
) : ViewModel() {

    // LiveData to observe recording state
    private val _recordingState = MutableLiveData<VoiceRecorderHelper<RecordingModelClass>?>()
    val recordingState: LiveData<VoiceRecorderHelper<RecordingModelClass>?> = _recordingState

    fun startRecording(filePath: String) {
        viewModelScope.launch {
            when (val result = voiceRecordingUseCase.startVoiceRecording(filePath)) {
                is VoiceRecorderHelper.Recording -> {
                    _recordingState.value = result
                }
                is VoiceRecorderHelper.Error -> {
                    _recordingState.value = result
                }
                else -> {}
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            when (val result = voiceRecordingUseCase.stopVoiceRecording()) {
                is VoiceRecorderHelper.Done -> {
                    _recordingState.value = result
                }
                is VoiceRecorderHelper.Error -> {
                    _recordingState.value = result
                }
                else -> {}
            }
        }
    }
}

