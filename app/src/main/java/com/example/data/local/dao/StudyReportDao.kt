package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.StudyReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyReportDao {
    @Query("SELECT * FROM study_reports WHERE userId = :userId ORDER BY dateString DESC, timestamp DESC")
    fun getReportsForUser(userId: Long): Flow<List<StudyReportEntity>>

    @Query("SELECT * FROM study_reports WHERE grade = :grade ORDER BY dateString DESC, timestamp DESC")
    fun getReportsForGrade(grade: Int): Flow<List<StudyReportEntity>>

    @Query("SELECT * FROM study_reports WHERE userId = :userId AND dateString = :dateString ORDER BY timestamp DESC")
    fun getReportsByDate(userId: Long, dateString: String): Flow<List<StudyReportEntity>>

    @Query("SELECT * FROM study_reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: Long): StudyReportEntity?

    @Query("SELECT * FROM study_reports ORDER BY dateString DESC, timestamp DESC")
    fun getAllReports(): Flow<List<StudyReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: StudyReportEntity): Long

    @Update
    suspend fun updateReport(report: StudyReportEntity)

    @Delete
    suspend fun deleteReport(report: StudyReportEntity)
}
