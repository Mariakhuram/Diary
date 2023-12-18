package com.mk.diary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mk.diary.data.entity.NoteViewDao
import com.mk.diary.domain.models.NoteViewModelClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [NoteViewModelClass::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(StringArrayListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // record dao
    abstract fun recordDao(): NoteViewDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            database.get().recordDao()
            applicationScope.launch {
                // pre-populating can be done here & not required in this case
            }
        }
    }

}