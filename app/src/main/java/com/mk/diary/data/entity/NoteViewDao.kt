package com.mk.diary.data.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mk.diary.domain.models.NoteViewModelClass

@Dao
interface NoteViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(model: NoteViewModelClass)

    @Update
    suspend fun updateData(model: NoteViewModelClass)

    @Delete
    suspend fun deleteData(model: NoteViewModelClass)

    @Query("Select * from data")
    fun getAllData(): LiveData<List<NoteViewModelClass>>

    @Query("DELETE FROM data")
    suspend fun deleteAll()

    @Query("SELECT * FROM data WHERE date = :date AND monthSt = :month AND year = :year")
    fun getDataForDateAndYear(date: String, month: String, year: String): LiveData<List<NoteViewModelClass>>

    @Query("SELECT * FROM data LIMIT 1 OFFSET :position")
    suspend fun getDataByPosition(position: Int): NoteViewModelClass?
}