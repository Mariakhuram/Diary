package com.mk.diary.presentation.viewmodels.noteview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.domain.usecase.NoteViewUseCase
import com.mk.diary.helpers.ResultCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel  @Inject constructor(private val useCase: NoteViewUseCase): ViewModel() {


    suspend fun getAllData(): LiveData<List<NoteViewModelClass>> {
        return useCase.getAllData()
    }
    suspend fun saveData(model: NoteViewModelClass): ResultCase<Unit> {
        return try {
            useCase.saveData(model)
            ResultCase.Success(Unit)
        } catch (e: Exception) {
            ResultCase.Error(e.message ?: "An error occurred")
        }
    }
    suspend fun delete(model: NoteViewModelClass):ResultCase<Unit>{
        return try {
            useCase.deleteData(model)
            ResultCase.Success(Unit)
        }catch (e:Exception){
            ResultCase.Error(e.message.toString())
        }
    }
    suspend fun update(model: NoteViewModelClass):ResultCase<Unit>{
       return try {
           useCase.updateData(model)
           ResultCase.Success(Unit)
       }catch (e:Exception){
           ResultCase.Error(e.message.toString())
       }
    }
    suspend fun getDataForDateAndYear(date: String,month:String ,year: String): LiveData<List<NoteViewModelClass>> {
        return useCase.getDataForDateAndYear(date, month,year)
    }

    suspend fun getItemByPosition(position:Int):NoteViewModelClass?{
        return useCase.getItemByPosition(position)
    }
    fun deleteAllData(){
        viewModelScope.launch {
            useCase.deleteAllData()
        }
    }
}