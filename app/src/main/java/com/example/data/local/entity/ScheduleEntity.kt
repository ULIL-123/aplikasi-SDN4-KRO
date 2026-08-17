package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val grade: Int, // 1 - 6
    val dayOfWeek: Int, // 1=Senin, 2=Selasa, 3=Rabu, 4=Kamis, 5=Jumat, 6=Sabtu
    val subjectName: String,
    val timeSlot: String, // e.g. "07:30 - 09:00 WIB"
    val teacherName: String,
    val topicBab: String,
    val booksToPrepare: String,
    val remoteAssignment: String = "",
    val isCompletedPreparation: Boolean = false
)
