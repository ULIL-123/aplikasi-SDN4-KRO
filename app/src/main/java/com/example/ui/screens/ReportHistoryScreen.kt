package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.StudyReportEntity
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportHistoryScreen(
    viewModel: MainViewModel,
    onNavigateToNewReport: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val reports by viewModel.userReports.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()

    val totalDurationMinutes = reports.sumOf { it.durationMinutes }
    val totalHours = totalDurationMinutes / 60
    val remMinutes = totalDurationMinutes % 60

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
                            text = "Rekap & Cetak Laporan Belajar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.White
                        )
                        Text(
                            text = "Ekspor PDF & Cetak Lembar Belajar Siswa",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA6F2D0)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (reports.isEmpty()) {
                                Toast.makeText(context, "Belum ada riwayat laporan untuk dicetak.", Toast.LENGTH_SHORT).show()
                            } else {
                                printAllReportsPdf(
                                    context = context,
                                    studentName = currentUser?.fullName ?: "Siswa SDN 4 Kronggen",
                                    grade = selectedGrade,
                                    nisn = currentUser?.nisn ?: "-",
                                    parentName = currentUser?.parentName ?: "-",
                                    reports = reports
                                )
                            }
                        },
                        modifier = Modifier.testTag("btn_print_all_pdf")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Cetak Semua ke PDF",
                                tint = SchoolBluePrimary
                            )
                        }
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

                // Official Kop Surat Resmi (Logo Grobogan di Kiri, Logo SD di Kanan)
                com.example.ui.components.OfficialKopHeaderCard()

                Spacer(modifier = Modifier.height(8.dp))

                // Summary Statistics Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Statistik Belajar Mandiri",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = SchoolBlueContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Total Laporan",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SchoolBluePrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${reports.size} Sesi",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = SchoolBluePrimary
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                color = LiteracyGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Total Jam Belajar",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LiteracyGreen
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${totalHours}j ${remMinutes}m",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = LiteracyGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (reports.isEmpty()) {
                                    Toast.makeText(context, "Belum ada riwayat laporan untuk dicetak.", Toast.LENGTH_SHORT).show()
                                } else {
                                    printAllReportsPdf(
                                        context = context,
                                        studentName = currentUser?.fullName ?: "Siswa SDN 4 Kronggen",
                                        grade = selectedGrade,
                                        nisn = currentUser?.nisn ?: "-",
                                        parentName = currentUser?.parentName ?: "-",
                                        reports = reports
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_print_official_document"),
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cetak Lembar Resmi Laporan Belajar (PDF)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (reports.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Belum ada rekaman laporan belajar",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Mulai belajar dan kirimkan laporan harian Anda sekarang.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = onNavigateToNewReport,
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Isi Laporan Sekarang")
                            }
                        }
                    }
                }
            } else {
                items(reports) { item ->
                    ReportItemCard(
                        report = item,
                        studentName = currentUser?.fullName ?: "Siswa",
                        parentName = currentUser?.parentName ?: "Orang Tua",
                        onShare = {
                            val textToShare = """
                                *SD NEGERI 4 KRONGGEN*
                                *Towards Literate School*
                                *LAPORAN BELAJAR REMOTE*
                                
                                📅 Tanggal: ${item.dateString} (${item.dayName})
                                👤 Siswa: ${currentUser?.fullName ?: "Siswa"} (Kelas $selectedGrade)
                                📖 Mapel: ${item.subject}
                                🔖 Materi: ${item.topicBab}
                                ⏱️ Durasi: ${item.durationMinutes} Menit (${item.startTime} - ${item.endTime} WIB)
                                📝 Ringkasan: ${item.learningSummary}
                                💡 Tingkat Pemahaman: ${item.understandingLevel}
                                👨‍👩‍👧 Catatan Wali: ${item.parentFeedback}
                            """.trimIndent()

                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, textToShare)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Bagikan ke WhatsApp"))
                        },
                        onPrintSingle = {
                            printSingleReportPdf(
                                context = context,
                                studentName = currentUser?.fullName ?: "Siswa",
                                grade = selectedGrade,
                                nisn = currentUser?.nisn ?: "-",
                                parentName = currentUser?.parentName ?: "-",
                                report = item
                            )
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun ReportItemCard(
    report: StudyReportEntity,
    studentName: String,
    parentName: String,
    onShare: () -> Unit,
    onPrintSingle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = SchoolBluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${report.dateString} (${report.dayName})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SchoolBluePrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SchoolBlueContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = SchoolBluePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${report.durationMinutes} Menit",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SchoolBluePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = report.subject,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (report.topicBab.isNotBlank()) {
                Text(
                    text = "Materi: ${report.topicBab}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = LiteracyGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.learningSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (report.parentFeedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Wali Murid: ${report.parentFeedback}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = SchoolBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions Row (WhatsApp & Print)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onShare,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_share_single_${report.id}")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = LiteracyGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", color = LiteracyGreen, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onPrintSingle,
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_print_single_${report.id}")
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cetak PDF", fontSize = 12.sp)
                }
            }
        }
    }
}

// Print / PDF Helper function using Android PrintManager & WebView
fun printAllReportsPdf(
    context: Context,
    studentName: String,
    grade: Int,
    nisn: String,
    parentName: String,
    reports: List<StudyReportEntity>
) {
    val htmlDocument = com.example.util.ReportPrintHelper.generateAllReportsHtml(
        context = context,
        studentName = studentName,
        grade = grade,
        nisn = nisn,
        parentName = parentName,
        reports = reports
    )

    executePrintJob(context, htmlDocument, "Rekap_Laporan_SDN4_Kronggen_$studentName")
}

fun printSingleReportPdf(
    context: Context,
    studentName: String,
    grade: Int,
    nisn: String,
    parentName: String,
    report: StudyReportEntity
) {
    val htmlDocument = com.example.util.ReportPrintHelper.generateHtmlReport(
        context = context,
        report = report,
        habit = null,
        student = null
    )

    executePrintJob(context, htmlDocument, "Laporan_A4_SDN4_Kronggen_${report.subject}_${report.dateString}")
}

private fun executePrintJob(context: Context, htmlContent: String, jobName: String) {
    val webView = WebView(context)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            val printAttributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(PrintAttributes.Resolution("pdf", "pdf", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build()
            printManager?.print(jobName, printAdapter, printAttributes)
        }
    }
    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
}
