package com.mk.mydiary.service

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException
import android.os.Handler
import com.mk.diary.helpers.voicerecordingplaying.RecordingModelClass
import java.util.concurrent.TimeUnit

object VoiceRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording: Boolean = false
    private var audioFilePath: String? = null
    private var durationHandler: Handler? = null
    private lateinit var model: RecordingModelClass
    var recordingDuration:Long = 0
    fun startRecording(filePath: String) {
        if (!isRecording) {
            audioFilePath = filePath
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder?.setOutputFile(audioFilePath)

            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                isRecording = true

                // Initialize duration tracking
                durationHandler = Handler()
                recordingDuration = 0
                durationHandler?.postDelayed(updateDurationRunnable, 1000) // Update every second
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception appropriately
                Log.e("VoiceRecorder", "Error starting recording: ${e.message}")
            }
        }
    }

    fun stopRecording(): RecordingModelClass? {
        if (isRecording) {
            try {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null
                isRecording = false

                // Stop duration tracking
                val formattedDuration = formatDuration(recordingDuration)
                model = audioFilePath?.let { RecordingModelClass(it, recordingDuration, formattedDuration) }!!
                durationHandler?.removeCallbacks(updateDurationRunnable)
                durationHandler = null
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception appropriately
                Log.e("VoiceRecorder", "Error stopping recording: ${e.message}")
            }
        }
        return model
    }

    // Runnable to update recording duration
    private val updateDurationRunnable = object : Runnable {
        override fun run() {
            recordingDuration += 1000 // Increment by 1 second
            Log.d("VoiceRecorder", "Recording duration: $recordingDuration ms")
            durationHandler?.postDelayed(this, 1000) // Schedule the next update
        }
    }

    private fun formatDuration(duration: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}
