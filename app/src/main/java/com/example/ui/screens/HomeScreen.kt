package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.LiveCalendarStrip
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.GoldenStarContainer
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.IndonesianRedContainer
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.LiteracyGreenContainer
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToSchedule: () -> Unit,
    onNavigateToHabits: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedDayOfWeek by viewModel.selectedDayOfWeek.collectAsStateWithLifecycle()
    val currentHabit by viewModel.currentHabit.collectAsStateWithLifecycle()
    val schedules by viewModel.schedulesForSelectedDay.collectAsStateWithLifecycle()
    val userReports by viewModel.userReports.collectAsStateWithLifecycle()

    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val timerSeconds by viewModel.timerSeconds.collectAsStateWithLifecycle()
    val timerStartTime by viewModel.timerStartTime.collectAsStateWithLifecycle()

    val todayReports = userReports.filter { it.dateString == selectedDate }
    val habitPercentage = currentHabit?.completionPercentage ?: 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Live Calendar Strip
        item {
            LiveCalendarStrip(
                selectedDate = selectedDate,
                selectedDayOfWeek = selectedDayOfWeek,
                onDateSelected = { dateStr, dayInt ->
                    viewModel.setSelectedDate(dateStr, dayInt)
                }
            )
        }

        // 2. Hero Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_remote_learning),
                        contentDescription = "Belajar Remote Bersama Orang Tua",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xEE0D47A1),
                                        Color(0x990D47A1),
                                        Color(0x22000000)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = LiteracyGreen,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "TOWARDS LITERATE SCHOOL",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Belajar Mandiri & Berkarakter",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = "Kolaborasi Siswa, Orang Tua & Guru Kelas $selectedGrade",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD6E4FF)
                        )
                    }
                }
            }
        }

        // 3. Live Study Timer & Quick Report Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isTimerRunning) IndonesianRedContainer else SchoolBlueContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isTimerRunning) Icons.Default.Timer else Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isTimerRunning) IndonesianRed else SchoolBluePrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isTimerRunning) "Sesi Belajar Sedang Berlangsung" else "Waktu Belajar Live Time",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isTimerRunning) "Mulai pukul $timerStartTime WIB" else "Mulai belajar untuk mencatat durasi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (isTimerRunning) {
                            val mins = timerSeconds / 60
                            val secs = timerSeconds % 60
                            Text(
                                text = String.format("%02d:%02d", mins, secs),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = IndonesianRed
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!isTimerRunning) {
                            Button(
                                onClick = { viewModel.startLiveTimer() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_start_study_timer"),
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Mulai Belajar", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModel.stopLiveTimer()
                                    onNavigateToReport()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_stop_and_report"),
                                colors = ButtonDefaults.buttonColors(containerColor = IndonesianRed),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Selesai & Lapor", fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = onNavigateToReport,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_quick_manual_report"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Isi Laporan", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // 4. Quick Highlights: 7KAIH & Report Status
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 7KAIH Mini Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToHabits() }
                        .testTag("card_7kaih_summary"),
                    colors = CardDefaults.cardColors(containerColor = LiteracyGreenContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "7 KAIH",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = LiteracyGreen
                            )
                            Text(
                                text = "$habitPercentage%",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = LiteracyGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { habitPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = LiteracyGreen,
                            trackColor = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (habitPercentage == 100) "🎉 Hebat! Semua tuntas" else "Lengkapi 7 kebiasaan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                // Daily Report Status Card
                val isReportSubmitted = todayReports.isNotEmpty()
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { if (isReportSubmitted) onNavigateToHistory() else onNavigateToReport() }
                        .testTag("card_report_status_summary"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isReportSubmitted) SchoolBlueContainer.copy(alpha = 0.4f) else GoldenStarContainer.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Laporan Hari Ini",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (isReportSubmitted) SchoolBluePrimary else GoldenStar
                            )
                            Icon(
                                imageVector = if (isReportSubmitted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                contentDescription = null,
                                tint = if (isReportSubmitted) SchoolBluePrimary else GoldenStar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isReportSubmitted) "${todayReports.size} Laporan Terkirim" else "Belum Mengisi",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isReportSubmitted) "Klik untuk cetak / bagikan" else "Klik untuk input belajar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 5. Today's Schedule & Tomorrow's Preparation Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Jadwal & Persiapan Pelajaran",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${viewModel.getDayNameFromIndex(selectedDayOfWeek)} (Kelas $selectedGrade)",
                                style = MaterialTheme.typography.bodySmall,
                                color = SchoolBluePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        TextButtonCustom(
                            text = "Lihat Semua",
                            onClick = onNavigateToSchedule,
                            tag = "btn_see_all_schedules"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (schedules.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hari Libur / Tidak ada jadwal tercatat.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        schedules.forEach { item ->
                            ScheduleListItemRow(
                                schedule = item,
                                onToggleCheck = { viewModel.toggleSchedulePreparation(item) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        // 6. 4 Fast Feature Navigation Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Menu Pembelajaran Remote SD 4 Kronggen",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureNavButton(
                        title = "Jadwal Pelajaran",
                        subtitle = "Senin s/d Sabtu",
                        icon = Icons.Default.Schedule,
                        tint = SchoolBluePrimary,
                        containerColor = SchoolBlueContainer.copy(alpha = 0.5f),
                        onClick = onNavigateToSchedule,
                        modifier = Modifier.weight(1f),
                        tag = "nav_btn_schedule"
                    )
                    FeatureNavButton(
                        title = "Gerakan 7KAIH",
                        subtitle = "7 Kebiasaan Hebat",
                        icon = Icons.Default.EmojiEvents,
                        tint = LiteracyGreen,
                        containerColor = LiteracyGreenContainer.copy(alpha = 0.5f),
                        onClick = onNavigateToHabits,
                        modifier = Modifier.weight(1f),
                        tag = "nav_btn_habits"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureNavButton(
                        title = "Cetak & Kirim",
                        subtitle = "PDF & WhatsApp",
                        icon = Icons.Default.Print,
                        tint = GoldenStar,
                        containerColor = GoldenStarContainer.copy(alpha = 0.5f),
                        onClick = onNavigateToHistory,
                        modifier = Modifier.weight(1f),
                        tag = "nav_btn_print_history"
                    )
                    FeatureNavButton(
                        title = "Pengingat Push",
                        subtitle = "Notifikasi Belajar",
                        icon = Icons.Default.Notifications,
                        tint = IndonesianRed,
                        containerColor = IndonesianRedContainer.copy(alpha = 0.5f),
                        onClick = onNavigateToNotifications,
                        modifier = Modifier.weight(1f),
                        tag = "nav_btn_notifications"
                    )
                }
            }
        }

        // 7. Towards Literate School Quote / Tips
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LiteracyGreen.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Pojok Literasi",
                        tint = LiteracyGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Towards Literate School Misi Kita",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = LiteracyGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"Membaca 15 menit setiap hari membuka cakrawala dunia dan mengasah budi pekerti luhur anak Indonesia.\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleListItemRow(
    schedule: com.example.data.local.entity.ScheduleEntity,
    onToggleCheck: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = schedule.isCompletedPreparation,
                onCheckedChange = { onToggleCheck() },
                colors = CheckboxDefaults.colors(
                    checkedColor = LiteracyGreen,
                    checkmarkColor = Color.White
                ),
                modifier = Modifier.testTag("chk_prep_${schedule.id}")
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.subjectName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${schedule.timeSlot} • ${schedule.teacherName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (schedule.topicBab.isNotBlank()) {
                    Text(
                        text = "Materi: ${schedule.topicBab}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SchoolBluePrimary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureNavButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun TextButtonCustom(
    text: String,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = SchoolBluePrimary
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = SchoolBluePrimary,
            modifier = Modifier.size(14.dp)
        )
    }
}
