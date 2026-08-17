package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE grade = :grade AND dayOfWeek = :dayOfWeek ORDER BY id ASC")
    fun getSchedulesByGradeAndDay(grade: Int, dayOfWeek: Int): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE grade = :grade ORDER BY dayOfWeek ASC, id ASC")
    fun getAllSchedulesByGrade(grade: Int): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schedules: List<ScheduleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Query("SELECT COUNT(*) FROM schedules")
    suspend fun getScheduleCount(): Int
}
