package com.mk.diary.domain.usecase

import androidx.lifecycle.LiveData
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.domain.repository.NoteViewRepository
import com.mk.diary.helpers.ResultCase
import javax.inject.Inject

class NoteViewUseCase @Inject constructor(private val repository: NoteViewRepository) {

    suspend fun saveData(noteViewModelClass: NoteViewModelClass): ResultCase<Unit> {
        return try {
            repository.saveData(noteViewModelClass)
            ResultCase.Success(Unit)
        } catch (e: Exception) {
            ResultCase.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun updateData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit> {
        return try {
            repository.updateData(noteViewModelClass)
            ResultCase.Success(Unit)
        }catch (e:Exception){
            ResultCase.Error(e.message.toString())
        }
    }

    suspend fun deleteData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit> {
        return try {
            repository.deleteData(noteViewModelClass)
            ResultCase.Success(Unit)
        }catch (e:Exception){
            ResultCase.Error(e.message.toString())
        }
    }

    suspend fun deleteAllData() {
        repository.deleteAllData()
    }

    suspend fun getAllData(): LiveData<List<NoteViewModelClass>> {
        return repository.getAllData()
    }

    suspend fun getDataForDateAndYear(
        date: String,
        year: String,
        month:String
    ): LiveData<List<NoteViewModelClass>> {
        return repository.getDataForDateAndYear(date,month,year)
    }

    suspend fun getItemByPosition(position:Int):NoteViewModelClass?{
        return repository.getItemByPosition(position)
    }
}