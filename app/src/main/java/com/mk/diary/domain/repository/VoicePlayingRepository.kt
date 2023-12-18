package com.mk.diary.domain.repository

import android.content.Context
import android.widget.ImageView

interface VoicePlayingRepository {
    suspend fun playVoice(context: Context, voice:String,imageView: ImageView)
    suspend fun stopVoice()
    suspend fun pauseVoice()
    suspend fun releaseVoice()
    suspend fun cm():Boolean
}