package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GradeSelectorDialog
import com.example.ui.components.SchoolTopBar
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel

enum class MainNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Beranda", Icons.Default.Home, Icons.Outlined.Home, "tab_nav_home"),
    SCHEDULE("Jadwal", Icons.Default.Schedule, Icons.Outlined.Schedule, "tab_nav_schedule"),
    HABITS("7 KAIH", Icons.Default.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_nav_habits"),
    REPORT("Lapor", Icons.Default.Assessment, Icons.Outlined.Assessment, "tab_nav_report"),
    RECAP("Rekap", Icons.Default.Print, Icons.Outlined.Print, "tab_nav_recap"),
    NOTIFICATIONS("Pengingat", Icons.Default.Notifications, Icons.Outlined.Notifications, "tab_nav_notifications")
}

@Composable
fun MainAppContainer(
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(MainNavTab.HOME) }
    var showGradeDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    if (showGradeDialog) {
        GradeSelectorDialog(
            currentGrade = selectedGrade,
            onGradeSelected = { grade ->
                viewModel.setSelectedGrade(grade)
                showGradeDialog = false
            },
            onDismiss = { showGradeDialog = false }
        )
    }

    if (showProfileDialog) {
        ProfileDetailDialog(
            user = currentUser,
            grade = selectedGrade,
            onDismiss = { showProfileDialog = false },
            onChangeGrade = {
                showProfileDialog = false
                showGradeDialog = true
            },
            onLogout = {
                showProfileDialog = false
                viewModel.logout()
                onLogout()
            }
        )
    }

    Scaffold(
        topBar = {
            SchoolTopBar(
                currentGrade = selectedGrade,
                userName = currentUser?.fullName ?: "Siswa SDN 4 Kronggen",
                userRole = currentUser?.role ?: "SISWA",
                onGradeClick = { showGradeDialog = true },
                onNotificationClick = { currentTab = MainNavTab.NOTIFICATIONS },
                onProfileClick = { showProfileDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SchoolBluePrimary,
                            selectedTextColor = SchoolBluePrimary,
                            indicatorColor = SchoolBlueContainer
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainNavTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToSchedule = { currentTab = MainNavTab.SCHEDULE },
                        onNavigateToHabits = { currentTab = MainNavTab.HABITS },
                        onNavigateToReport = { currentTab = MainNavTab.REPORT },
                        onNavigateToHistory = { currentTab = MainNavTab.RECAP },
                        onNavigateToNotifications = { currentTab = MainNavTab.NOTIFICATIONS }
                    )
                }
                MainNavTab.SCHEDULE -> {
                    ScheduleScreen(
                        viewModel = viewModel,
                        onGradeChangeClick = { showGradeDialog = true }
                    )
                }
                MainNavTab.HABITS -> {
                    SevenHabitsScreen(
                        viewModel = viewModel,
                        onNavigateToReport = { currentTab = MainNavTab.REPORT }
                    )
                }
                MainNavTab.REPORT -> {
                    ReportScreen(
                        viewModel = viewModel,
                        onReportSubmitted = { currentTab = MainNavTab.RECAP }
                    )
                }
                MainNavTab.RECAP -> {
                    ReportHistoryScreen(
                        viewModel = viewModel,
                        onNavigateToNewReport = { currentTab = MainNavTab.REPORT }
                    )
                }
                MainNavTab.NOTIFICATIONS -> {
                    NotificationScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDetailDialog(
    user: com.example.data.local.entity.UserEntity?,
    grade: Int,
    onDismiss: () -> Unit,
    onChangeGrade: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = SchoolBluePrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profil Akun Pembelajaran", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SchoolBlueContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = user?.fullName ?: "Siswa Hebat",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SchoolBluePrimary
                        )
                        Text(
                            text = "NISN: ${user?.nisn ?: "-"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Peran: ${user?.role ?: "SISWA"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tingkat: Kelas $grade",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = SchoolBluePrimary
                        )
                        if (!user?.parentName.isNullOrBlank()) {
                            Text(
                                text = "Wali: ${user?.parentName} (${user?.parentPhone ?: "-"})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onChangeGrade() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = SchoolBluePrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ganti Tingkat Kelas Siswa (1 - 6)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onLogout() },
                    colors = CardDefaults.cardColors(containerColor = IndonesianRed.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = IndonesianRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ganti Akun / Keluar",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = IndonesianRed
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", fontWeight = FontWeight.Bold)
            }
        }
    )
}
