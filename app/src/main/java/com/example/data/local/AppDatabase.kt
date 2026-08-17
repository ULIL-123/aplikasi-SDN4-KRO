package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.HabitDao
import com.example.data.local.dao.ReminderDao
import com.example.data.local.dao.ScheduleDao
import com.example.data.local.dao.StudyReportDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.HabitRecordEntity
import com.example.data.local.entity.ReminderEntity
import com.example.data.local.entity.ScheduleEntity
import com.example.data.local.entity.StudyReportEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ScheduleEntity::class,
        HabitRecordEntity::class,
        StudyReportEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun habitDao(): HabitDao
    abstract fun studyReportDao(): StudyReportDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sdn4_kronggen_remote_learning.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
