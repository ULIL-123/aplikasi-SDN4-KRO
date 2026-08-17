package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.LiveCalendarStrip
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.IndonesianRedContainer
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportScreen(
    viewModel: MainViewModel,
    onReportSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedDayOfWeek by viewModel.selectedDayOfWeek.collectAsStateWithLifecycle()
    val schedules by viewModel.schedulesForSelectedDay.collectAsStateWithLifecycle()

    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val timerSeconds by viewModel.timerSeconds.collectAsStateWithLifecycle()
    val timerStartTime by viewModel.timerStartTime.collectAsStateWithLifecycle()

    val currentTimeStr = remember {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date())
    }

    var subjectName by remember { mutableStateOf("") }
    var topicBab by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableIntStateOf(45) }
    var startTime by remember { mutableStateOf(if (isTimerRunning) timerStartTime else "08:00") }
    var endTime by remember { mutableStateOf(currentTimeStr) }
    var activityDescription by remember { mutableStateOf("") }
    var obstaclesNotes by remember { mutableStateOf("Tidak ada kendala, belajar mandiri lancar.") }
    var parentNotes by remember { mutableStateOf("Didampingi dan diperiksa dengan baik.") }
    var studentMood by remember { mutableStateOf("Sangat Baik") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = SchoolBluePrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Laporan Belajar Remote",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = "Pencatatan Aktivitas, Waktu & Evaluasi Belajar Mandiri",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA6F2D0)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Kelas $selectedGrade",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Live Calendar Strip
        LiveCalendarStrip(
            selectedDate = selectedDate,
            selectedDayOfWeek = selectedDayOfWeek,
            onDateSelected = { dateStr, dayInt ->
                viewModel.setSelectedDate(dateStr, dayInt)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Official Kop Surat Resmi Preview (Logo Grobogan di Kiri, Logo SD di Kanan)
            com.example.ui.components.OfficialKopHeaderCard()

            Spacer(modifier = Modifier.height(8.dp))

            // Live Timer Assistant Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTimerRunning) IndonesianRedContainer else SchoolBlueContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (isTimerRunning) IndonesianRed else SchoolBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isTimerRunning) "Live Stopwatch Berjalan: ${timerSeconds / 60}m ${timerSeconds % 60}s" else "Live Time Otomatis",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isTimerRunning) IndonesianRed else SchoolBluePrimary
                            )
                            Text(
                                text = if (isTimerRunning) "Dimulai pukul $timerStartTime WIB" else "Gunakan tombol di bawah untuk mencatat durasi real",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (isTimerRunning) {
                        Button(
                            onClick = {
                                val recordedMins = maxOf(1, (timerSeconds / 60).toInt())
                                durationMinutes = recordedMins
                                startTime = timerStartTime
                                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                viewModel.stopLiveTimer()
                                Toast.makeText(context, "Sesi selesai! Durasi $recordedMins menit diterapkan.", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IndonesianRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_stop_timer_in_report")
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Selesai", fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.startLiveTimer() },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_start_timer_in_report")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mulai", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Form Content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Quick Subject Chips from Today's Schedule
                    Text(
                        text = "Pilih Mata Pelajaran Hari Ini:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(schedules) { sch ->
                            FilterChip(
                                selected = subjectName == sch.subjectName,
                                onClick = {
                                    subjectName = sch.subjectName
                                    if (sch.topicBab.isNotBlank()) {
                                        topicBab = sch.topicBab
                                    }
                                    if (sch.remoteAssignment.isNotBlank()) {
                                        activityDescription = sch.remoteAssignment
                                    }
                                },
                                label = { Text(sch.subjectName, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SchoolBluePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Manual Subject Field (if other)
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { subjectName = it },
                        label = { Text("Nama Mata Pelajaran") },
                        placeholder = { Text("misal: Bahasa Indonesia / Matematika") },
                        leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_report_subject"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Topic / Bab
                    OutlinedTextField(
                        value = topicBab,
                        onValueChange = { topicBab = it },
                        label = { Text("Materi / Bab / Tema Belajar") },
                        placeholder = { Text("misal: Bab 3 - Bilangan Cacah & Pecahan") },
                        leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_report_topic"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Duration Slider
                    Text(
                        text = "Durasi Belajar: $durationMinutes Menit ($startTime - $endTime WIB)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Slider(
                        value = durationMinutes.toFloat(),
                        onValueChange = { durationMinutes = it.toInt() },
                        valueRange = 10f..180f,
                        steps = 16,
                        colors = SliderDefaults.colors(
                            thumbColor = SchoolBluePrimary,
                            activeTrackColor = SchoolBluePrimary
                        ),
                        modifier = Modifier.testTag("slider_report_duration")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Activity Description
                    OutlinedTextField(
                        value = activityDescription,
                        onValueChange = { activityDescription = it },
                        label = { Text("Uraian Aktivitas & Tugas yang Dikerjakan") },
                        placeholder = { Text("Menonton video pembelajaran, mengerjakan LKS halaman 24 nomor 1-10, dan membaca rangkuman.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_report_activity"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mood / Respon Siswa
                    Text(
                        text = "Tingkat Pemahaman Siswa:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Sangat Baik", "Baik", "Cukup", "Perlu Bimbingan").forEach { mood ->
                            FilterChip(
                                selected = studentMood == mood,
                                onClick = { studentMood = mood },
                                label = { Text(mood, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LiteracyGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Kendala
                    OutlinedTextField(
                        value = obstaclesNotes,
                        onValueChange = { obstaclesNotes = it },
                        label = { Text("Kendala yang Dihadapi (Bila Ada)") },
                        leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null, tint = GoldenStar) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_report_obstacles"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Catatan Orang Tua & Tanda Tangan Virtual
                    OutlinedTextField(
                        value = parentNotes,
                        onValueChange = { parentNotes = it },
                        label = { Text("Catatan & Konfirmasi Orang Tua") },
                        leadingIcon = { Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = SchoolBluePrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_report_parentnotes"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (subjectName.isBlank()) {
                                Toast.makeText(context, "Silakan pilih atau isi nama mata pelajaran!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val summary = if (activityDescription.isNotBlank()) {
                                "$activityDescription (Kendala: $obstaclesNotes)"
                            } else {
                                "Mengerjakan tugas mandiri sesuai panduan guru. Kendala: $obstaclesNotes"
                            }

                            viewModel.submitStudyReport(
                                subject = subjectName,
                                topicBab = topicBab,
                                startTime = startTime,
                                endTime = endTime,
                                durationMinutes = durationMinutes,
                                learningSummary = summary,
                                photoPresetType = "REMOTE_STUDY_1",
                                photoUri = "",
                                parentFeedback = parentNotes,
                                understandingLevel = studentMood,
                                parentSignatureName = currentUser?.parentName ?: "Orang Tua Siswa",
                                onSuccess = {
                                    Toast.makeText(context, "Laporan belajar berhasil dikirim dan tersimpan di database!", Toast.LENGTH_LONG).show()
                                    onReportSubmitted()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_submit_report"),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Kirim Laporan Belajar",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // WhatsApp Direct Share Button
                    OutlinedButton(
                        onClick = {
                            if (subjectName.isBlank()) {
                                Toast.makeText(context, "Silakan pilih mata pelajaran terlebih dahulu", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
                            val textToShare = """
                                *LAPORAN BELAJAR REMOTE SD NEGERI 4 KRONGGEN*
                                *Towards Literate School*
                                
                                📅 Tanggal: $selectedDate
                                👤 Nama Siswa: ${currentUser?.fullName ?: "Siswa"}
                                🏫 Tingkat: Kelas $selectedGrade
                                📖 Mata Pelajaran: $subjectName
                                🔖 Materi/Bab: $topicBab
                                ⏱️ Durasi: $durationMinutes Menit ($startTime - $endTime WIB)
                                📝 Aktivitas: $activityDescription
                                💡 Tingkat Pemahaman: $studentMood
                                ⚠️ Kendala: $obstaclesNotes
                                👨‍👩‍👧 Konfirmasi Orang Tua: $parentNotes (${currentUser?.parentName ?: "Wali Murid"})
                                
                                _Laporan otomatis dari Aplikasi Pembelajaran Remote SDN 4 Kronggen_
                            """.trimIndent()

                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, textToShare)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Kirim Laporan via WhatsApp Guru/Wali")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_share_report_whatsapp"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = LiteracyGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bagikan ke WhatsApp Guru / Grup Kelas", color = LiteracyGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
