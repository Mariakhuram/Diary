package com.mk.mydiary.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.IOException

class VoiceService private constructor(val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentFilePath: String? = null

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.isLooping = false
    }
    companion object {
        private var instance: VoiceService? = null
        fun getInstance(context: Context): VoiceService {
            if (instance == null) {
                synchronized(VoiceService::class.java) {
                    if (instance == null) {
                        instance = VoiceService(context)
                    }
                }
            }
            return instance!!
        }
    }

    fun play(param: String) {
        if (param == currentFilePath && mediaPlayer?.isPlaying == false) {
            // If the provided data source is the same and the media player is not playing, resume
            mediaPlayer?.start()
        } else {
            // If the provided data source is different or the media player is not initialized, start fresh
            stop()

            currentFilePath = param
            try {
                mediaPlayer?.setDataSource(param)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception appropriately
                Log.e("VoiceService", "Error playing audio: ${e.message}")
            }
        }
    }
    fun pause() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        currentFilePath = null
    }

    fun release() {
        mediaPlayer?.release()
        instance = null
    }
}
