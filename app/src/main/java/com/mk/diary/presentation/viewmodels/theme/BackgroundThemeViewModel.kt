package com.mk.diary.presentation.viewmodels.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.diary.domain.usecase.BackgroundThemeUseCase
import com.mk.diary.helpers.ResultCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackgroundThemeViewModel @Inject constructor(private val useCase: BackgroundThemeUseCase)
    :ViewModel(){
    private val _classicLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()

    private val _allLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()

    fun getClassic(){
        viewModelScope.launch {
            _classicLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progress

            try {
                val data = useCase.classicBackground()
                _classicLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _classicLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }

    fun getAll(){
        viewModelScope.launch {
            _allLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progress

            try {
                val data = useCase.allBackground()
                for (d in data){
                    d
                }
                _allLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _allLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }
    }
    val allLiveData: LiveData<ResultCase<List<String>>>
        get() = _allLiveData
    val classicLiveData: LiveData<ResultCase<List<String>>>
        get() = _classicLiveData

    private val _holidayLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getHoliday(){
        viewModelScope.launch {
            _holidayLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.holidayBackground()
                _holidayLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _holidayLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val holidayLiveData: LiveData<ResultCase<List<String>>>
        get() = _holidayLiveData

    private val _cuteLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getCute(){
        viewModelScope.launch {
            _cuteLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.cuteBackground()
                _cuteLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _cuteLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val cuteLiveData: LiveData<ResultCase<List<String>>>
        get() = _cuteLiveData

    private val _colorLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getColor(){
        viewModelScope.launch {
            _colorLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.colorBackground()
                _colorLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _colorLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val colorLiveData: LiveData<ResultCase<List<String>>>
        get() = _colorLiveData

    private val _simpleLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getSimple(){
        viewModelScope.launch {
            _simpleLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.simpleBackground()
                _simpleLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _simpleLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val simpleLiveData: LiveData<ResultCase<List<String>>>
        get() = _simpleLiveData
    //
    private val _sliderThemeLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getSlidingTheme(){
        viewModelScope.launch {
            _sliderThemeLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.getSliderTheme()
                _sliderThemeLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _sliderThemeLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val sliderThemeLiveData: LiveData<ResultCase<List<String>>>
        get() = _sliderThemeLiveData

    private val _applyThemeLiveData : MutableLiveData<ResultCase<List<String>>>  = MutableLiveData()
    fun getApplyTheme(){
        viewModelScope.launch {
            _applyThemeLiveData.postValue(ResultCase.Loading) // Notify UI that the operation is in progres
            try {
                val data = useCase.getApplyTheme()
                _applyThemeLiveData.postValue(ResultCase.Success(data))
            } catch (e: Exception) {
                _applyThemeLiveData.postValue(ResultCase.Error("Error found due to ${e.message}"))
            }
        }

    }
    val applyThemeLiveData: LiveData<ResultCase<List<String>>>
        get() = _applyThemeLiveData
}
