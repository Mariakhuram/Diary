package com.mk.diary.data.repositoryimp

import android.media.MediaRecorder
import android.os.Handler
import android.util.Log
import com.mk.diary.domain.repository.VoiceRecordingRepository
import com.mk.diary.helpers.VoiceRecorderHelper
import com.mk.diary.helpers.voicerecordingplaying.RecordingModelClass
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VoiceRecordingRepositoryImp @Inject constructor() : VoiceRecordingRepository {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording: Boolean = false
    private var audioFilePath: String? = null
    private var durationHandler: Handler? = null
    private lateinit var model: RecordingModelClass
    private var recordingDuration: Long = 0

    override suspend fun startVoiceRecording(filePath: String): VoiceRecorderHelper<RecordingModelClass> {
        return try {
            if (!isRecording) {
                audioFilePath = filePath
                mediaRecorder = MediaRecorder()
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecorder?.setOutputFile(audioFilePath)
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                isRecording = true

                // Initialize duration tracking
                durationHandler = Handler()
                recordingDuration = 0
                durationHandler?.postDelayed(updateDurationRunnable, 1000) // Update every second

                VoiceRecorderHelper.Recording
            } else {
                VoiceRecorderHelper.Error("Recording is already in progress")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            VoiceRecorderHelper.Error("Error starting recording: ${e.message}")
        }
    }

    override suspend fun stopVoiceRecording(): VoiceRecorderHelper<RecordingModelClass> {
        return try {
            if (isRecording) {
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null
                isRecording = false
                // Stop duration tracking
                val formattedDuration = formatDuration(recordingDuration)
                model = RecordingModelClass(audioFilePath.orEmpty(), recordingDuration, formattedDuration)
                durationHandler?.removeCallbacks(updateDurationRunnable)
                durationHandler = null

                VoiceRecorderHelper.Done(model)
            } else {
                VoiceRecorderHelper.Error("No recording in progress to stop")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            VoiceRecorderHelper.Error("Error stopping recording: ${e.message}")
        }
    }

    private val updateDurationRunnable = object : Runnable {
        override fun run() {
            recordingDuration += 1000 // Increment by 1 second
            Log.d("VoiceRecorder", "Recording duration: ${recordingDuration} ms")
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

