package com.mk.diary.helpers

sealed class VoiceRecorderHelper<out T> {
    data class Done<out T>(val data: T) : VoiceRecorderHelper<T>()
    object Recording:VoiceRecorderHelper<Nothing>()
    object Stop:VoiceRecorderHelper<Nothing>()
    data class Error(val message: String) : VoiceRecorderHelper<Nothing>()
    object Loading : VoiceRecorderHelper<Nothing>()
}
