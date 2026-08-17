package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Login, 1 = Register

    // Login Fields
    var loginIdentifier by remember { mutableStateOf("abimanyu") }
    var loginPassword by remember { mutableStateOf("123456") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Register Fields
    var regFullName by remember { mutableStateOf("") }
    var regUsername by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regRole by remember { mutableStateOf("SISWA") } // SISWA or ORANG_TUA
    var regNisn by remember { mutableStateOf("") }
    var regGrade by remember { mutableIntStateOf(4) }
    var regParentName by remember { mutableStateOf("") }
    var regParentPhone by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header Banner with School Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SchoolBluePrimary, Color(0xFF072A60))
                        )
                    )
                    .padding(top = 36.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // LOGO KABUPATEN GROBOGAN (KIRI)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_kabupaten_grobogan),
                                contentDescription = "Logo Pemerintah Kabupaten Grobogan",
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // LOGO SD NEGERI 4 KRONGGEN (KANAN)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sdn4_kronggen_logo),
                                contentDescription = "Logo SD Negeri 4 Kronggen",
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "SD NEGERI 4 KRONGGEN",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "PEMERINTAH KABUPATEN GROBOGAN • DINAS PENDIDIKAN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color(0xFFFFE082),
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFA6F2D0).copy(alpha = 0.25f),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text(
                            text = "Towards Literate School",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFA6F2D0),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Aplikasi Pembelajaran Remote, Jadwal & Gerakan 7KAIH",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD6E4FF),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Main Card Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Tab Selector
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = SchoolBlueContainer,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    "Masuk (Login)",
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 0) SchoolBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.testTag("tab_login")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "Daftar (Register)",
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 1) SchoolBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.testTag("tab_register")
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedTab == 0) {
                        // --- LOGIN FORM ---
                        Text(
                            text = "Masuk ke Akun Pembelajaran",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Silakan masukkan Username atau NISN siswa",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = loginIdentifier,
                            onValueChange = { loginIdentifier = it },
                            label = { Text("Username atau NISN") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_login_username"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = { loginPassword = it },
                            label = { Text("Kata Sandi (Password)") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_login_password"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                viewModel.login(loginIdentifier, loginPassword) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (success) {
                                        onAuthSuccess()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_login"),
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Masuk Sekarang",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Demo Quick Login Shortcuts
                        Text(
                            text = "Akses Cepat Demo Akun:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    loginIdentifier = "abimanyu"
                                    loginPassword = "123456"
                                    viewModel.login("abimanyu", "123456") { _, _ -> onAuthSuccess() }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("demo_student_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Siswa (Kls 4)", fontSize = 11.sp, maxLines = 1)
                            }

                            OutlinedButton(
                                onClick = {
                                    loginIdentifier = "bapak_sugeng"
                                    loginPassword = "123456"
                                    viewModel.login("bapak_sugeng", "123456") { _, _ -> onAuthSuccess() }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("demo_parent_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Wali Murid", fontSize = 11.sp, maxLines = 1)
                            }

                            OutlinedButton(
                                onClick = {
                                    loginIdentifier = "bu_siti"
                                    loginPassword = "123456"
                                    viewModel.login("bu_siti", "123456") { _, _ -> onAuthSuccess() }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("demo_teacher_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Guru", fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    } else {
                        // --- REGISTER FORM ---
                        Text(
                            text = "Pendaftaran Peserta Didik / Orang Tua",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Lengkapi data untuk terhubung dengan sistem pembelajaran remote",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Role Selector
                        Text("Daftar Sebagai:", style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = regRole == "SISWA",
                                onClick = { regRole = "SISWA" },
                                label = { Text("Siswa / Anak") },
                                leadingIcon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                modifier = Modifier.testTag("role_siswa")
                            )
                            FilterChip(
                                selected = regRole == "ORANG_TUA",
                                onClick = { regRole = "ORANG_TUA" },
                                label = { Text("Orang Tua / Wali") },
                                leadingIcon = { Icon(Icons.Default.FamilyRestroom, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                modifier = Modifier.testTag("role_orangtua")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Full Name
                        OutlinedTextField(
                            value = regFullName,
                            onValueChange = { regFullName = it },
                            label = { Text("Nama Lengkap Siswa") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_fullname"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Username & Password
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = regUsername,
                                onValueChange = { regUsername = it },
                                label = { Text("Username") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_reg_username"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_reg_password"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Grade Selector (Kelas 1 - 6)
                        Text("Pilih Tingkat Kelas Siswa:", style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(1, 2, 3, 4, 5, 6).forEach { grade ->
                                FilterChip(
                                    selected = regGrade == grade,
                                    onClick = { regGrade = grade },
                                    label = { Text("Kls $grade", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SchoolBluePrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // NISN & Parent Info
                        OutlinedTextField(
                            value = regNisn,
                            onValueChange = { regNisn = it },
                            label = { Text("NISN (Nomor Induk Siswa Nasional)") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_nisn"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regParentName,
                            onValueChange = { regParentName = it },
                            label = { Text("Nama Orang Tua / Wali Murid") },
                            leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_parentname"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regParentPhone,
                            onValueChange = { regParentPhone = it },
                            label = { Text("No. WhatsApp Orang Tua (Untuk Kirim Laporan)") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reg_parentphone"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                viewModel.register(
                                    fullName = regFullName,
                                    username = regUsername,
                                    password = regPassword,
                                    role = regRole,
                                    nisn = regNisn,
                                    grade = regGrade,
                                    parentName = regParentName,
                                    parentPhone = regParentPhone
                                ) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    if (success) {
                                        onAuthSuccess()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_register"),
                            colors = ButtonDefaults.buttonColors(containerColor = LiteracyGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Daftarkan Siswa Sekarang",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SD Negeri 4 Kronggen • Towards Literate School\nKabupaten Grobogan, Jawa Tengah",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}
