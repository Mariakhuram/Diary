package com.mk.diary.domain.repository

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.LiveData
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.helpers.ResultCase

interface NoteViewRepository {
    suspend fun saveData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit>
    suspend fun updateData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit>
    suspend fun deleteData(noteViewModelClass: NoteViewModelClass):ResultCase<Unit>
    suspend fun deleteAllData()
    suspend fun getAllData():LiveData<List<NoteViewModelClass>>
    suspend fun getDataForDateAndYear(date:String,year:String,month:String):LiveData<List<NoteViewModelClass>>
    suspend fun getItemByPosition(position:Int):NoteViewModelClass?
}