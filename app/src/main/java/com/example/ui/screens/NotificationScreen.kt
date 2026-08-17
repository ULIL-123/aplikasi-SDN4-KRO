package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.ReminderEntity
import com.example.notification.NotificationHelper
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.IndonesianRedContainer
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun NotificationScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Izin notifikasi diaktifkan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Izin notifikasi ditolak.", Toast.LENGTH_SHORT).show()
            }
        }
    )

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
                            text = "Pengingat & Notifikasi Belajar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = "SD Negeri 4 Kronggen • Jadwal & Gerakan 7KAIH",
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
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Permission Card (for Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                    if (!hasPermission) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = IndonesianRedContainer.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Aktifkan Izin Notifikasi",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = IndonesianRed
                                    )
                                    Text(
                                        text = "Agar aplikasi dapat mengirimkan pengingat jadwal belajar dan evaluasi harian.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp
                                    )
                                }
                                Button(
                                    onClick = {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndonesianRed),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Izinkan", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Quick Live Test Notification
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tes Push Notifikasi Sistem",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Tekan tombol di bawah untuk menguji notifikasi pengingat SDN 4 Kronggen langsung di status bar ponsel.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.triggerTestNotification(
                                        title = "SDN 4 Kronggen: Waktunya Belajar! 🔔",
                                        message = "Jadwal Kelas $selectedGrade siap dimulai. Buka aplikasi untuk mencatat durasi belajar hari ini."
                                    )
                                    Toast.makeText(context, "Notifikasi pengingat terkirim ke status bar!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_test_notify_study"),
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Tes Pengingat", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.triggerTestNotification(
                                        title = "Evaluasi 7KAIH & Literasi 🌟",
                                        message = "Yuk periksa 7 Kebiasaan Hebat dan membaca 15 menit hari ini (Towards Literate School)."
                                    )
                                    Toast.makeText(context, "Notifikasi 7KAIH terkirim!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_test_notify_habits"),
                                colors = ButtonDefaults.buttonColors(containerColor = LiteracyGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Tes 7KAIH", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Reminders from Database
            items(reminders) { reminder ->
                val (icon, iconColor) = when (reminder.category) {
                    "STUDY_MORNING" -> Pair(Icons.Default.WbSunny, GoldenStar)
                    "HABIT_CHECK" -> Pair(Icons.Default.EmojiEvents, LiteracyGreen)
                    "LITERACY_READING" -> Pair(Icons.Default.AutoStories, SchoolBluePrimary)
                    "NIGHT_PREPARATION" -> Pair(Icons.Default.Bedtime, Color(0xFF5E35B1))
                    else -> Pair(Icons.Default.Alarm, SchoolBluePrimary)
                }

                NotificationReminderCard(
                    reminder = reminder,
                    icon = icon,
                    iconTint = iconColor,
                    onToggle = { viewModel.toggleReminder(reminder) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun NotificationReminderCard(
    reminder: ReminderEntity,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = reminder.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SchoolBlueContainer
                    ) {
                        val timeStr = String.format("%02d:%02d WIB", reminder.timeHour, reminder.timeMinute)
                        Text(
                            text = timeStr,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SchoolBluePrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reminder.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            Switch(
                checked = reminder.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SchoolBluePrimary,
                    checkedTrackColor = SchoolBlueContainer
                ),
                modifier = Modifier.testTag("switch_reminder_${reminder.id}")
            )
        }
    }
}
