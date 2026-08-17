package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_records")
data class HabitRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val dateString: String, // YYYY-MM-DD
    val dayName: String, // Senin, Selasa, dll
    // 7 Kebiasaan Anak Indonesia Hebat (7KAIH)
    val habit1WakeUp: Boolean = false,        // Bangun Pagi & Merapikan Tempat Tidur
    val habit2Worship: Boolean = false,       // Beribadah Sesuai Agama
    val habit3Exercise: Boolean = false,      // Berolahraga / Aktivitas Fisik
    val habit4Literacy: Boolean = false,      // Gemar Membaca & Literasi (Towards Literate School)
    val habit5HealthyFood: Boolean = false,   // Makan Makanan Sehat & Bergizi
    val habit6HelpParents: Boolean = false,   // Membantu Orang Tua di Rumah
    val habit7EarlySleep: Boolean = false,    // Tidur Tepat Waktu & Refleksi Harian
    val literacyBookTitle: String = "",
    val literacyMinutes: Int = 15,
    val parentNotes: String = "",
    val completionPercentage: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
