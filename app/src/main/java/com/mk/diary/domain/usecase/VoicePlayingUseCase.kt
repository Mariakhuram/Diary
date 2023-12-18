package com.mk.diary.domain.usecase

import android.content.Context
import android.widget.ImageView
import com.mk.diary.domain.repository.VoicePlayingRepository
import javax.inject.Inject

class VoicePlayingUseCase @Inject constructor(private val repository: VoicePlayingRepository) {

    suspend fun playVoice(context: Context, voice:String,imageView: ImageView){
        repository.playVoice(context,voice,imageView)
    }
    suspend fun stopVoice(){
        repository.stopVoice()
    }
    suspend fun pauseVoice(){
        repository.pauseVoice()
    }
    suspend fun releaseVoice(){
        repository.releaseVoice()
    }

}