package com.example.data.util

import com.example.data.local.AppDatabase
import com.example.data.local.entity.HabitRecordEntity
import com.example.data.local.entity.ReminderEntity
import com.example.data.local.entity.ScheduleEntity
import com.example.data.local.entity.StudyReportEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object SampleDataSeeder {

    suspend fun seedInitialDataIfEmpty(database: AppDatabase) = withContext(Dispatchers.IO) {
        val userDao = database.userDao()
        val scheduleDao = database.scheduleDao()
        val reminderDao = database.reminderDao()
        val habitDao = database.habitDao()
        val reportDao = database.studyReportDao()

        // 1. Seed Users if empty
        if (userDao.getUserByUsername("abimanyu") == null) {
            val defaultStudent = UserEntity(
                username = "abimanyu",
                passwordHash = "123456",
                fullName = "Abimanyu Pratama",
                role = "SISWA",
                nisn = "0123456789",
                grade = 4,
                parentName = "Bapak Sugeng Raharjo",
                parentPhone = "081234567890",
                avatarId = 1
            )
            val studentId = userDao.insertUser(defaultStudent)

            val defaultParent = UserEntity(
                username = "bapak_sugeng",
                passwordHash = "123456",
                fullName = "Sugeng Raharjo (Wali Murid)",
                role = "ORANG_TUA",
                nisn = "0123456789",
                grade = 4,
                parentName = "Bapak Sugeng Raharjo",
                parentPhone = "081234567890",
                avatarId = 2
            )
            userDao.insertUser(defaultParent)

            val defaultTeacher = UserEntity(
                username = "bu_siti",
                passwordHash = "123456",
                fullName = "Siti Rahmawati, S.Pd (Wali Kelas 4)",
                role = "GURU",
                nisn = "198504122010012015",
                grade = 4,
                parentName = "-",
                parentPhone = "085678901234",
                avatarId = 3
            )
            userDao.insertUser(defaultTeacher)

            // Seed initial habit & report for today and yesterday
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = sdf.format(Date())
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = sdf.format(cal.time)

            habitDao.insertOrUpdateHabit(
                HabitRecordEntity(
                    userId = studentId,
                    dateString = yesterdayStr,
                    dayName = "Kemarin",
                    habit1WakeUp = true,
                    habit2Worship = true,
                    habit3Exercise = true,
                    habit4Literacy = true,
                    habit5HealthyFood = true,
                    habit6HelpParents = true,
                    habit7EarlySleep = true,
                    literacyBookTitle = "Kisah Si Kancil & Budaya Literasi",
                    literacyMinutes = 20,
                    parentNotes = "Sangat tertib dan rajin membaca buku!",
                    completionPercentage = 100
                )
            )

            habitDao.insertOrUpdateHabit(
                HabitRecordEntity(
                    userId = studentId,
                    dateString = todayStr,
                    dayName = "Hari Ini",
                    habit1WakeUp = true,
                    habit2Worship = true,
                    habit3Exercise = true,
                    habit4Literacy = true,
                    habit5HealthyFood = true,
                    habit6HelpParents = false,
                    habit7EarlySleep = false,
                    literacyBookTitle = "Mengenal Ekosistem Hutan Indonesia",
                    literacyMinutes = 15,
                    parentNotes = "Sedang menyelesaikan persiapan jadwal besok.",
                    completionPercentage = 71
                )
            )

            reportDao.insertReport(
                StudyReportEntity(
                    userId = studentId,
                    studentName = "Abimanyu Pratama",
                    grade = 4,
                    dateString = yesterdayStr,
                    dayName = "Kemarin",
                    subject = "IPAS (Ilmu Pengetahuan Alam & Sosial)",
                    topicBab = "Bab 3: Gaya di Sekitar Kita (Gaya Magnet & Gravitasi)",
                    startTime = "19:00",
                    endTime = "20:15",
                    durationMinutes = 75,
                    learningSummary = "Membaca ringkasan bab magnet, mengerjakan latihan uji pemahaman halaman 45, dan menyiapkan alat peraga magnet untuk praktek besok.",
                    photoPresetType = "REMOTE_STUDY_1",
                    parentFeedback = "Anak belajar secara mandiri dengan pendampingan orang tua.",
                    understandingLevel = "Sangat Baik",
                    parentSignatureName = "Sugeng Raharjo",
                    isSubmittedToTeacher = true
                )
            )
        }

        // 2. Seed Schedules if empty (Senin s/d Sabtu for Grades 1 to 6)
        if (scheduleDao.getScheduleCount() == 0) {
            val schedules = mutableListOf<ScheduleEntity>()

            // Days: 1=Senin, 2=Selasa, 3=Rabu, 4=Kamis, 5=Jumat, 6=Sabtu
            for (grade in 1..6) {
                // Senin
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 1,
                        subjectName = "Upacara Bendera & Pendidikan Pancasila (PPKn)",
                        timeSlot = "07:00 - 08:45 WIB",
                        teacherName = "Bapak Budi Santoso, S.Pd",
                        topicBab = "Nilai-Nilai Luhur Pancasila dalam Kehidupan Sehari-hari",
                        booksToPrepare = "Buku Paket PPKn Kurikulum Merdeka, Buku Tulis, Seragam Merah Putih Lengkap Bertopi & Dasi",
                        remoteAssignment = "Menuliskan 3 contoh gotong royong di lingkungan sekitar"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 1,
                        subjectName = "Bahasa Indonesia (Literasi Membaca)",
                        timeSlot = "09:00 - 10:30 WIB",
                        teacherName = "Ibu Siti Rahmawati, S.Pd",
                        topicBab = "Bab 2: Menyimak Cerita & Menemukan Ide Pokok Paragraf",
                        booksToPrepare = "Buku Bahasa Indonesia, Buku Cerita Literasi, Kamus Ringkas",
                        remoteAssignment = "Membaca 2 halaman buku literasi dan menulis rangkuman 1 paragraf"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 1,
                        subjectName = "Pendidikan Agama & Budi Pekerti",
                        timeSlot = "10:45 - 12:15 WIB",
                        teacherName = "Bapak Ahmad Fauzi, S.Pd.I",
                        topicBab = "Perilaku Terpuji & Menghormati Orang Tua",
                        booksToPrepare = "Buku PAI, Juz Amma / Kitab Suci, Buku Catatan Amalan",
                        remoteAssignment = "Praktik membaca doa harian bersama orang tua di rumah"
                    )
                )

                // Selasa
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 2,
                        subjectName = "Matematika (Numerasi & Logika)",
                        timeSlot = "07:30 - 09:30 WIB",
                        teacherName = "Ibu Sri Wahyuni, M.Pd",
                        topicBab = if (grade <= 3) "Penjumlahan & Pengurangan Bilangan Cacah" else "Pecahan Senilai & Operasi Hitung",
                        booksToPrepare = "Buku Matematika Kurikulum Merdeka, Penggaris, Buku Kotak/Milimeter",
                        remoteAssignment = "Latihan soal mandiri nomor 1 sampai 5 halaman 32"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 2,
                        subjectName = "Seni Budaya & Prakarya (SBdP)",
                        timeSlot = "10:00 - 11:30 WIB",
                        teacherName = "Ibu Maya Anggraini, S.Sn",
                        topicBab = "Mengenal Motif Tradisional & Melipat Kertas Origami",
                        booksToPrepare = "Buku Gambar A4, Krayon / Pensil Warna, Kertas Lipat",
                        remoteAssignment = "Membuat sketsa motif flora/fauna sederhana"
                    )
                )

                // Rabu
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 3,
                        subjectName = "IPAS (Ilmu Pengetahuan Alam & Sosial)",
                        timeSlot = "07:30 - 09:30 WIB",
                        teacherName = "Bapak Haryanto, S.Pd",
                        topicBab = "Bagian Tumbuh-Tumbuhan & Fotosintesis Sederhana",
                        booksToPrepare = "Buku IPAS, Daun segar/spesimen untuk observasi, Lembar Catatan",
                        remoteAssignment = "Mengamati 2 jenis daun di halaman rumah dan mencatat ciri-cirinya"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 3,
                        subjectName = "Bahasa Jawa (Muatan Lokal)",
                        timeSlot = "10:00 - 11:30 WIB",
                        teacherName = "Bapak Setyo Pambudi, S.Pd",
                        topicBab = "Unggah-Ungguh Basa & Tembang Macapat Pocung",
                        booksToPrepare = "Buku Basa Jawa Kirtya Basa / Sastri Jawa, Pepak Basa Jawa",
                        remoteAssignment = "Berbicara krama alus kepada orang tua saat pamit belajar"
                    )
                )

                // Kamis
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 4,
                        subjectName = "PJOK (Pendidikan Jasmani & Kesehatan)",
                        timeSlot = "07:00 - 09:00 WIB",
                        teacherName = "Bapak Eko Prasetyo, S.Pd",
                        topicBab = "Gerak Dasar Lokomotor & Permainan Bola Kecil",
                        booksToPrepare = "Kaos Olahraga SD 4 Kronggen, Sepatu Olahraga, Botol Minum Air Putih, Handuk Kecil",
                        remoteAssignment = "Melakukan peregangan 10 menit di pagi hari"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 4,
                        subjectName = "Bahasa Inggris / Literasi Global",
                        timeSlot = "09:30 - 11:00 WIB",
                        teacherName = "Ibu Dian Permata, S.Pd",
                        topicBab = "Unit 3: My Family & Daily Activities",
                        booksToPrepare = "English Handbook, Flashcards kosakata, Buku Latihan",
                        remoteAssignment = "Menulis 5 kalimat kegiatan keluarga dalam bahasa Inggris"
                    )
                )

                // Jumat
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 5,
                        subjectName = "Jumat Literasi & Bersih Lingkungan (Towards Literate School)",
                        timeSlot = "07:00 - 08:30 WIB",
                        teacherName = "Tim Literasi SD 4 Kronggen",
                        topicBab = "Membaca Bersama 15 Menit di Pojok Baca & Gerakan Bersih Sekolah",
                        booksToPrepare = "Buku Bacaan Favorit / Cerita Rakyat, Seragam Batik Sekolah, Tempat Sampah Mini",
                        remoteAssignment = "Mencatat judul buku dan hikmah moral yang didapat"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 5,
                        subjectName = "Pendidikan Karakter & 7 KAIH",
                        timeSlot = "08:45 - 10:30 WIB",
                        teacherName = "Wali Kelas & Guru BK",
                        topicBab = "Penguatan 7 Kebiasaan Anak Indonesia Hebat di Rumah",
                        booksToPrepare = "Buku Jurnal 7KAIH, Buku Komunikasi Orang Tua & Guru",
                        remoteAssignment = "Evaluasi checklist mingguan bersama ayah/ibu"
                    )
                )

                // Sabtu
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 6,
                        subjectName = "Pramuka Siaga / Penggalang & Ekstrakurikuler",
                        timeSlot = "07:30 - 09:30 WIB",
                        teacherName = "Kakak Pembina Pramuka SDN 4 Kronggen",
                        topicBab = "Dwi Darma / Dasa Darma & Keterampilan Tali Temali / Semaphore",
                        booksToPrepare = "Seragam Pramuka Lengkap (Kacu/Setangan Leher, Topi, Tali Pandu), Buku SKU",
                        remoteAssignment = "Praktik simpul mati dan simpul pangkal di rumah"
                    )
                )
                schedules.add(
                    ScheduleEntity(
                        grade = grade,
                        dayOfWeek = 6,
                        subjectName = "Review Pembelajaran Remote & Refleksi Pekanan",
                        timeSlot = "10:00 - 11:30 WIB",
                        teacherName = "Wali Kelas",
                        topicBab = "Kompilasi Laporan Mingguan & Cetak Rekapitulasi",
                        booksToPrepare = "Semua Buku PR & Map Portofolio Siswa",
                        remoteAssignment = "Menyiapkan cetak laporan belajar untuk ditandatangani orang tua"
                    )
                )
            }

            scheduleDao.insertAll(schedules)
        }

        // 3. Seed Reminders if empty
        if (reminderDao.getReminderCount() == 0) {
            val reminders = listOf(
                ReminderEntity(
                    title = "🌅 7KAIH: Bangun Pagi & Ibadah Subuh",
                    message = "Selamat pagi siswa hebat SD 4 Kronggen! Rapikan tempat tidur, beribadah, dan minum air putih sehat.",
                    timeHour = 5,
                    timeMinute = 0,
                    isEnabled = true,
                    category = "7KAIH"
                ),
                ReminderEntity(
                    title = "🥗 7KAIH: Sarapan Bergizi & Olahraga Pagi",
                    message = "Lakukan senam ringan dan sarapan sehat sebelum memulai persiapan jadwal belajar.",
                    timeHour = 6,
                    timeMinute = 15,
                    isEnabled = true,
                    category = "7KAIH"
                ),
                ReminderEntity(
                    title = "📚 Persiapan Jadwal Pelajaran Hari Ini",
                    message = "Cek kembali tas sekolah, buku pelajaran, alat tulis, dan PR yang akan dibahas hari ini.",
                    timeHour = 6,
                    timeMinute = 45,
                    isEnabled = true,
                    category = "PERSIAPAN_BESOK"
                ),
                ReminderEntity(
                    title = "📖 Towards Literate School: 15 Menit Membaca",
                    message = "Luangkan waktu 15 menit membaca buku cerita/pengetahuan untuk memperluas wawasanmu!",
                    timeHour = 16,
                    timeMinute = 30,
                    isEnabled = true,
                    category = "7KAIH"
                ),
                ReminderEntity(
                    title = "⏰ Waktu Belajar Remote Malam (Live Time)",
                    message = "Saatnya belajar malam didampingi orang tua! Aktifkan timer dan pelajari materi/bab esok hari.",
                    timeHour = 19,
                    timeMinute = 0,
                    isEnabled = true,
                    category = "JADWAL_BELAJAR"
                ),
                ReminderEntity(
                    title = "📝 Pengingat: Kirim Laporan Belajar & 7KAIH",
                    message = "Ayah/Bunda dan Ananda, jangan lupa selesaikan pengisian laporan belajar hari ini dan upload foto kegiatan.",
                    timeHour = 20,
                    timeMinute = 15,
                    isEnabled = true,
                    category = "LAPORAN"
                ),
                ReminderEntity(
                    title = "🌙 7KAIH: Istirahat Tepat Waktu & Refleksi",
                    message = "Matikan gadget, gosok gigi, dan tidur tepat waktu agar esok hari tubuh segar dan siap belajar!",
                    timeHour = 21,
                    timeMinute = 0,
                    isEnabled = true,
                    category = "7KAIH"
                )
            )
            reminderDao.insertAll(reminders)
        }
    }
}
