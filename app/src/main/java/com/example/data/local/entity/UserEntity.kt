package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val fullName: String,
    val role: String, // "SISWA", "ORANG_TUA", "GURU"
    val nisn: String,
    val grade: Int, // 1 to 6
    val parentName: String,
    val parentPhone: String,
    val avatarId: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
