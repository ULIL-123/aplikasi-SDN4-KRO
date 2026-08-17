package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_reports")
data class StudyReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val studentName: String,
    val grade: Int,
    val dateString: String, // YYYY-MM-DD
    val dayName: String, // Senin, Selasa, dll
    val subject: String,
    val topicBab: String,
    val startTime: String, // e.g. "19:00"
    val endTime: String, // e.g. "20:30"
    val durationMinutes: Int,
    val learningSummary: String,
    val photoUri: String = "",
    val photoPresetType: String = "REMOTE_STUDY_1", // Built-in realistic photos or custom
    val parentFeedback: String = "",
    val understandingLevel: String = "Sangat Baik", // Sangat Baik, Baik, Cukup, Perlu Bimbingan
    val parentSignatureName: String = "",
    val isSubmittedToTeacher: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
