package com.mk.diary.presentation.viewmodels.playing

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.diary.domain.usecase.VoicePlayingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class VoicePlayingViewModel @Inject constructor(private val useCase: VoicePlayingUseCase)
    :ViewModel(){

    fun playVoice(context: Context,voice:String,imageView: ImageView){
        viewModelScope.launch(Dispatchers.Default) {
            useCase.playVoice(context,voice,imageView)
        }
    }
    fun stopVoice(){
        viewModelScope.launch(Dispatchers.Default) {
            useCase.stopVoice()
        }
    }
    fun pauseVoice(){
        viewModelScope.launch(Dispatchers.Default) {
            useCase.pauseVoice()
        }
    }
    fun releaseVoice(){
        viewModelScope.launch(Dispatchers.Default) {
            useCase.releaseVoice()
        }
    }

}