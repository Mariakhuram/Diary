package com.mk.diary.data.repositoryimp

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import com.mk.diary.R
import com.mk.diary.domain.repository.VoicePlayingRepository
import com.mk.mydiary.service.VoiceService
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.SharedPrefObj
import com.mk.mydiary.utils.companion.Static
import java.io.IOException
import javax.inject.Inject

class VoicePlayingRepositoryImp @Inject constructor():VoicePlayingRepository {
    private val mediaPlayer :MediaPlayer? by lazy {  MediaPlayer() }
    private var currentFilePath: String? = null
     var isPlaybackComplete = false
    override suspend fun playVoice(context: Context,voice: String,image:ImageView) {
        if (voice == currentFilePath && mediaPlayer?.isPlaying == false) {
            // If the provided data source is the same and the media player is not playing, resume
            mediaPlayer?.start()
        } else {
            // If the provided data source is different or the media player is not initialized, start fresh
            stopVoice()
            currentFilePath = voice
            try {
                mediaPlayer?.setDataSource(voice)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                mediaPlayer?.setOnCompletionListener {
                    image.setImageResource(R.drawable.next_arrow)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception appropriately
                Log.e("VoiceService", "Error playing audio: ${e.message}")
            }
        }
    }
    override suspend fun stopVoice() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        currentFilePath = null
    }
    override suspend fun pauseVoice() {
        mediaPlayer?.pause()
    }
    override suspend fun releaseVoice() {
    }

    override suspend fun cm(): Boolean {
        return isPlaybackComplete
    }

}