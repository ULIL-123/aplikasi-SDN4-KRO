package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.LiveCalendarStrip
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.LiteracyGreenContainer
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel

data class HabitItemMeta(
    val index: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tag: String,
    val color: Color
)

@Composable
fun SevenHabitsScreen(
    viewModel: MainViewModel,
    onNavigateToReport: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedDayOfWeek by viewModel.selectedDayOfWeek.collectAsStateWithLifecycle()
    val currentHabit by viewModel.currentHabit.collectAsStateWithLifecycle()

    val habit = currentHabit
    val percentage = habit?.completionPercentage ?: 0

    var literacyTitle by remember(habit?.literacyBookTitle) {
        mutableStateOf(habit?.literacyBookTitle ?: "")
    }
    var literacyMinutes by remember(habit?.literacyMinutes) {
        mutableIntStateOf(habit?.literacyMinutes ?: 15)
    }
    var parentNotes by remember(habit?.parentNotes) {
        mutableStateOf(habit?.parentNotes ?: "")
    }

    val habitItems = listOf(
        HabitItemMeta(
            index = 1,
            title = "1. Bangun Pagi & Merapikan Tempat Tidur",
            description = "Membiasakan disiplin mandiri sejak awal bangun pagi hari.",
            icon = Icons.Default.WbSunny,
            tag = "habit_1_wakeup",
            color = GoldenStar
        ),
        HabitItemMeta(
            index = 2,
            title = "2. Beribadah Sesuai Agama",
            description = "Melaksanakan sholat/ibadah dan berdoa sebelum/sesudah aktivitas.",
            icon = Icons.Default.Mosque,
            tag = "habit_2_worship",
            color = SchoolBluePrimary
        ),
        HabitItemMeta(
            index = 3,
            title = "3. Berolahraga / Aktivitas Fisik",
            description = "Senam ringan, peregangan, atau bermain aktif di luar ruangan.",
            icon = Icons.Default.FitnessCenter,
            tag = "habit_3_exercise",
            color = IndonesianRed
        ),
        HabitItemMeta(
            index = 4,
            title = "4. Gemar Membaca & Literasi (Towards Literate School)",
            description = "Membaca buku cerita, artikel pengetahuan, atau ensiklopedia.",
            icon = Icons.Default.AutoStories,
            tag = "habit_4_literacy",
            color = LiteracyGreen
        ),
        HabitItemMeta(
            index = 5,
            title = "5. Makan Makanan Sehat & Bergizi",
            description = "Sarapan sehat, makan sayur, buah, dan minum air putih yang cukup.",
            icon = Icons.Default.Restaurant,
            tag = "habit_5_healthyfood",
            color = Color(0xFFE65100)
        ),
        HabitItemMeta(
            index = 6,
            title = "6. Membantu Orang Tua di Rumah",
            description = "Membantu menyapu, merapikan meja belajar, atau mencuci piring sendiri.",
            icon = Icons.Default.CleaningServices,
            tag = "habit_6_helpparents",
            color = Color(0xFF00897B)
        ),
        HabitItemMeta(
            index = 7,
            title = "7. Istirahat & Tidur Tepat Waktu",
            description = "Tidur malam tepat waktu dan refleksi rasa syukur hari ini.",
            icon = Icons.Default.Bedtime,
            tag = "habit_7_earlysleep",
            color = Color(0xFF5E35B1)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = LiteracyGreen,
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
                            text = "7 Kebiasaan Anak Indonesia Hebat",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = "Gerakan 7KAIH • SD Negeri 4 Kronggen",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA6F2D0)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Overall Progress Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                    text = "Capaian Kebiasaan Hari Ini",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Siswa: ${currentUser?.fullName ?: "Siswa Hebat"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (percentage == 100) LiteracyGreen else SchoolBluePrimary
                            ) {
                                Text(
                                    text = "$percentage%",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { percentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (percentage == 100) LiteracyGreen else SchoolBluePrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (percentage == 100) "🌟 Luar biasa! Seluruh 7 Kebiasaan Anak Indonesia Hebat telah terpenuhi hari ini." else "Tandai kebiasaan positif yang telah Ananda laksanakan hari ini bersama keluarga.",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (percentage == 100) LiteracyGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 7 Habits Checklist Cards
            habitItems.forEach { item ->
                val isChecked = when (item.index) {
                    1 -> habit?.habit1WakeUp == true
                    2 -> habit?.habit2Worship == true
                    3 -> habit?.habit3Exercise == true
                    4 -> habit?.habit4Literacy == true
                    5 -> habit?.habit5HealthyFood == true
                    6 -> habit?.habit6HelpParents == true
                    7 -> habit?.habit7EarlySleep == true
                    else -> false
                }

                item {
                    HabitCheckCard(
                        meta = item,
                        isChecked = isChecked,
                        onToggle = { viewModel.toggleHabitItem(item.index, !isChecked) }
                    )
                }

                // If Literacy item is checked, show Towards Literate School extra form
                if (item.index == 4 && isChecked) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = LiteracyGreenContainer.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = LiteracyGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Catatan Literasi Harian (Towards Literate School)",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = LiteracyGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = literacyTitle,
                                    onValueChange = { literacyTitle = it },
                                    label = { Text("Judul Buku / Cerita yang Dibaca") },
                                    placeholder = { Text("misal: Cerita Rakyat Grobogan / Dongeng Si Kancil") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("input_literacy_book_title"),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Durasi Membaca: $literacyMinutes Menit",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )

                                Slider(
                                    value = literacyMinutes.toFloat(),
                                    onValueChange = { literacyMinutes = it.toInt() },
                                    valueRange = 5f..60f,
                                    steps = 10,
                                    colors = SliderDefaults.colors(
                                        thumbColor = LiteracyGreen,
                                        activeTrackColor = LiteracyGreen
                                    ),
                                    modifier = Modifier.testTag("slider_literacy_minutes")
                                )

                                OutlinedTextField(
                                    value = parentNotes,
                                    onValueChange = { parentNotes = it },
                                    label = { Text("Catatan / Respon Orang Tua") },
                                    placeholder = { Text("misal: Anak sangat antusias membaca bab 2") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("input_literacy_parent_notes"),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        viewModel.updateLiteracyDetails(literacyTitle, literacyMinutes, parentNotes)
                                        Toast.makeText(context, "Catatan literasi berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .testTag("btn_save_literacy_notes"),
                                    colors = ButtonDefaults.buttonColors(containerColor = LiteracyGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Simpan Literasi", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HabitCheckCard(
    meta: HabitItemMeta,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onToggle() }
            .testTag("card_${meta.tag}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) meta.color.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isChecked) 1.dp else 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isChecked) meta.color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = meta.icon,
                    contentDescription = null,
                    tint = if (isChecked) meta.color else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meta.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = meta.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = meta.color,
                    checkmarkColor = Color.White
                ),
                modifier = Modifier.testTag("chk_${meta.tag}")
            )
        }
    }
}
