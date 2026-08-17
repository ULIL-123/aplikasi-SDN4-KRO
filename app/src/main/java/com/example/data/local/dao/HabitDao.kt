package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.HabitRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit_records WHERE userId = :userId AND dateString = :dateString LIMIT 1")
    fun getHabitByDate(userId: Long, dateString: String): Flow<HabitRecordEntity?>

    @Query("SELECT * FROM habit_records WHERE userId = :userId AND dateString = :dateString LIMIT 1")
    suspend fun getHabitByDateDirect(userId: Long, dateString: String): HabitRecordEntity?

    @Query("SELECT * FROM habit_records WHERE userId = :userId ORDER BY dateString DESC")
    fun getAllHabitsForUser(userId: Long): Flow<List<HabitRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHabit(habit: HabitRecordEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitRecordEntity)
}
