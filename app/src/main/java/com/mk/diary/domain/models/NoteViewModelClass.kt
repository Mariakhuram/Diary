package com.mk.diary.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mk.diary.database.StringArrayListConverter
import java.io.Serializable

@Entity(tableName = "data")
data class NoteViewModelClass (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "dayOfWeek") val dayOfWeek: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "monthSt") val monthString: String?,
    @TypeConverters(StringArrayListConverter::class)
    @ColumnInfo(name = "imageOne") val listOfImages: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "voice") val voice: String?,
    @ColumnInfo(name = "voiceDuration") val voiceDuration: String?,
    @ColumnInfo(name = "title") val mainTitle: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "descriptionUnderline") val descriptionUnderline: String?,
    @ColumnInfo(name = "descriptionHighLight") val descriptionHighLight: String?,
    @TypeConverters(StringArrayListConverter::class)
    @ColumnInfo(name = "tagTitle") val tagTitle:ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "noteViewStatus") val noteViewStatus: Int?,
    @ColumnInfo(name = "backgroundTheme") val backgroundTheme: Int?,
    @ColumnInfo(name = "fontFamily") val fontFamily: String?,
    @ColumnInfo(name = "capitalizationLetter") val capitalizationLetter: String?,
    @ColumnInfo(name = "textSize") val textSize: Float?,
    @ColumnInfo(name = "textColor") val textColor: Int?,
    @ColumnInfo(name = "gravityStyle") val gravityStyle: Int?,
    @TypeConverters(StringArrayListConverter::class)
    @ColumnInfo(name = "textBulletStyle") val textBulletStyle: ArrayList<String> = arrayListOf(),
    ):Serializable {

}