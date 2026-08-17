package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val timeHour: Int,
    val timeMinute: Int,
    val isEnabled: Boolean = true,
    val category: String, // "JADWAL_BELAJAR", "7KAIH", "LAPORAN", "PERSIAPAN_BESOK"
    val daysMask: Int = 127 // All days
)
