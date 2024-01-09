package com.mk.diary.domain.models

import androidx.room.ColumnInfo
import java.io.Serializable

data class VoiceRecordingModelClass(
    @ColumnInfo(name = "voceNm") val voiceName: String?,
    @ColumnInfo(name = "voiceM") val voicePath: String?,
    @ColumnInfo(name = "duraM") val duration: String?):Serializable
