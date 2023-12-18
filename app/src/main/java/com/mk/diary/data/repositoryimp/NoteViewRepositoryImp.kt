package com.mk.diary.data.repositoryimp

import androidx.lifecycle.LiveData
import com.mk.diary.data.entity.NoteViewDao
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.domain.repository.NoteViewRepository
import com.mk.diary.helpers.ResultCase
import javax.inject.Inject

class NoteViewRepositoryImp @Inject constructor(private val dao: NoteViewDao
): NoteViewRepository {
    override suspend fun saveData(noteViewModelClass: NoteViewModelClass): ResultCase<Unit> {
        return try {
            dao.insertData(noteViewModelClass)
            ResultCase.Success(Unit)
        } catch (e: Exception) {
            ResultCase.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit> {
        return try {
            dao.updateData(noteViewModelClass)
            ResultCase.Success(Unit)
        }catch (e:Exception){
            ResultCase.Error(e.message.toString())
        }
    }

    override suspend fun deleteData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit>{
        return try {
            dao.deleteData(noteViewModelClass)
            ResultCase.Success(Unit)
        }catch (e:Exception){
            ResultCase.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteAllData() {
        dao.deleteAll()
    }

    override suspend fun getAllData(): LiveData<List<NoteViewModelClass>> {
        return dao.getAllData()
    }




    override suspend fun getDataForDateAndYear(
        date: String,
        year: String,
        month:String
    ): LiveData<List<NoteViewModelClass>> {
        return dao.getDataForDateAndYear(date,month,year)
    }

    override suspend fun getItemByPosition(position: Int): NoteViewModelClass? {
        return dao.getDataByPosition(position)
    }
}