package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.R
import com.example.data.local.entity.HabitRecordEntity
import com.example.data.local.entity.StudyReportEntity
import com.example.data.local.entity.UserEntity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportPrintHelper {

    private var groboganBase64Cache: String? = null
    private var sdn4Base64Cache: String? = null

    private fun decodeAndCompressLogo(context: Context, resId: Int): String {
        return try {
            val options = BitmapFactory.Options().apply {
                inSampleSize = 2
            }
            val originalBitmap = BitmapFactory.decodeResource(context.resources, resId, options)
            if (originalBitmap != null) {
                val scaled = Bitmap.createScaledBitmap(originalBitmap, 150, 150, true)
                val outputStream = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                val bytes = outputStream.toByteArray()
                if (scaled != originalBitmap) {
                    originalBitmap.recycle()
                }
                scaled.recycle()
                "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun getGroboganLogoBase64(context: Context): String {
        groboganBase64Cache?.let { return it }
        val str = decodeAndCompressLogo(context, R.drawable.logo_kabupaten_grobogan)
        groboganBase64Cache = str
        return str
    }

    fun getSdn4LogoBase64(context: Context): String {
        sdn4Base64Cache?.let { return it }
        val str = decodeAndCompressLogo(context, R.drawable.sdn4_kronggen_logo)
        sdn4Base64Cache = str
        return str
    }

    fun generateOfficialKopHtml(context: Context): String {
        val logoGrobogan = getGroboganLogoBase64(context)
        val logoSdn4 = getSdn4LogoBase64(context)

        return """
            <table class="kop-table" style="width: 100%; border-collapse: collapse; border: none; margin-bottom: 2px;">
                <tr>
                    <td style="width: 16%; text-align: left; vertical-align: middle; border: none; padding: 2px;">
                        <img src="$logoGrobogan" alt="Logo Pemkab Grobogan" style="width: 78px; height: 78px; object-fit: contain;" />
                    </td>
                    <td style="width: 68%; text-align: center; vertical-align: middle; border: none; padding: 0 4px;">
                        <div style="font-family: 'Arial', sans-serif; font-size: 13pt; font-weight: bold; color: #000; letter-spacing: 0.5px; line-height: 1.2;">PEMERINTAH KABUPATEN GROBOGAN</div>
                        <div style="font-family: 'Arial', sans-serif; font-size: 13pt; font-weight: bold; color: #000; letter-spacing: 0.5px; line-height: 1.2;">DINAS PENDIDIKAN DAN KEBUDAYAAN</div>
                        <div style="font-family: 'Arial', sans-serif; font-size: 16pt; font-weight: 900; color: #072A60; margin: 3px 0; line-height: 1.2;">SD NEGERI 4 KRONGGEN</div>
                        <div style="font-family: 'Arial', sans-serif; font-size: 9pt; color: #222; line-height: 1.3;">Alamat: Desa Kronggen, Kec. Brati, Kab. Grobogan, Jawa Tengah 58153</div>
                        <div style="font-family: 'Arial', sans-serif; font-size: 9pt; color: #1E6F50; font-weight: bold; font-style: italic; margin-top: 2px;">Towards Literate School • Gerakan 7 Kebiasaan Anak Indonesia Hebat (7KAIH)</div>
                    </td>
                    <td style="width: 16%; text-align: right; vertical-align: middle; border: none; padding: 2px;">
                        <img src="$logoSdn4" alt="Logo SD Negeri 4 Kronggen" style="width: 78px; height: 78px; object-fit: contain;" />
                    </td>
                </tr>
            </table>
            <div style="border-top: 3px solid #000; border-bottom: 1px solid #000; height: 2px; margin-top: 6px; margin-bottom: 16px;"></div>
        """.trimIndent()
    }

    fun generateHtmlReport(
        context: Context,
        report: StudyReportEntity,
        habit: HabitRecordEntity?,
        student: UserEntity?
    ): String {
        val studentName = student?.fullName ?: report.studentName
        val nisn = student?.nisn ?: "-"
        val grade = report.grade
        val parentName = student?.parentName ?: report.parentSignatureName.ifEmpty { "Orang Tua / Wali" }
        val date = report.dateString
        val day = report.dayName
        val kopHtml = generateOfficialKopHtml(context)

        val habit1Icon = if (habit?.habit1WakeUp == true) "✅ Terlaksana" else "❌ Belum"
        val habit2Icon = if (habit?.habit2Worship == true) "✅ Terlaksana" else "❌ Belum"
        val habit3Icon = if (habit?.habit3Exercise == true) "✅ Terlaksana" else "❌ Belum"
        val habit4Icon = if (habit?.habit4Literacy == true) "✅ Terlaksana (${habit.literacyMinutes} Menit: ${habit.literacyBookTitle.ifEmpty { "Buku Cerita / Literasi" }})" else "❌ Belum"
        val habit5Icon = if (habit?.habit5HealthyFood == true) "✅ Terlaksana" else "❌ Belum"
        val habit6Icon = if (habit?.habit6HelpParents == true) "✅ Terlaksana" else "❌ Belum"
        val habit7Icon = if (habit?.habit7EarlySleep == true) "✅ Terlaksana" else "❌ Belum"

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>Laporan Pembelajaran Remote A4 - SD Negeri 4 Kronggen</title>
                <style>
                    @page {
                        size: A4 portrait;
                        margin: 12mm 15mm;
                    }
                    @media print {
                        body {
                            margin: 0;
                            padding: 0;
                        }
                    }
                    body {
                        font-family: 'Times New Roman', Times, serif, 'Segoe UI', Arial, sans-serif;
                        color: #1a1a1a;
                        line-height: 1.4;
                        font-size: 11pt;
                        margin: 10px;
                        background: #ffffff;
                    }
                    .title-doc {
                        text-align: center;
                        margin-bottom: 14px;
                    }
                    .title-doc h4 {
                        margin: 0;
                        font-size: 13pt;
                        font-family: Arial, sans-serif;
                        font-weight: bold;
                        text-decoration: underline;
                        color: #000;
                        text-transform: uppercase;
                    }
                    .title-doc small {
                        font-family: Arial, sans-serif;
                        font-size: 9.5pt;
                        color: #444;
                    }
                    table.info-table {
                        width: 100%;
                        margin-bottom: 14px;
                        border-collapse: collapse;
                    }
                    table.info-table td {
                        padding: 4px 6px;
                        font-size: 10.5pt;
                        vertical-align: top;
                    }
                    table.info-table td.label {
                        width: 22%;
                        font-weight: bold;
                        color: #111;
                    }
                    table.data-table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-bottom: 16px;
                    }
                    table.data-table th {
                        background-color: #072A60;
                        color: white;
                        text-align: left;
                        padding: 7px 10px;
                        font-size: 10.5pt;
                        font-family: Arial, sans-serif;
                        border: 1px solid #072A60;
                    }
                    table.data-table td {
                        padding: 7px 10px;
                        border: 1px solid #94A3B8;
                        font-size: 10.5pt;
                    }
                    .section-title {
                        font-family: Arial, sans-serif;
                        font-weight: bold;
                        font-size: 11.5pt;
                        color: #072A60;
                        margin-top: 14px;
                        margin-bottom: 6px;
                        border-bottom: 1.5px solid #CBD5E1;
                        padding-bottom: 3px;
                        text-transform: uppercase;
                    }
                    .habit-list {
                        list-style-type: none;
                        padding-left: 0;
                        margin-bottom: 18px;
                    }
                    .habit-list li {
                        padding: 5px 8px;
                        margin-bottom: 3px;
                        background: #F8FAFC;
                        border: 1px solid #E2E8F0;
                        border-radius: 4px;
                        font-size: 10pt;
                    }
                    .signature-row {
                        margin-top: 30px;
                        width: 100%;
                        display: table;
                    }
                    .signature-col {
                        display: table-cell;
                        width: 50%;
                        text-align: center;
                        font-size: 10.5pt;
                    }
                    .sig-space {
                        height: 60px;
                    }
                    .badge {
                        display: inline-block;
                        padding: 2px 7px;
                        border-radius: 4px;
                        font-weight: bold;
                        font-size: 8.5pt;
                        background: #E0F2FE;
                        color: #0369A1;
                    }
                </style>
            </head>
            <body>
                $kopHtml

                <div class="title-doc">
                    <h4>LEMBAR PELAPORAN BELAJAR REMOTE & PEMBIASAAN 7 KAIH</h4>
                    <small>Hari/Tanggal: <b>$day, $date</b> &nbsp;|&nbsp; Status: <span class="badge">TERVERIFIKASI SISTEM</span></small>
                </div>

                <table class="info-table">
                    <tr>
                        <td class="label">Nama Siswa</td>
                        <td style="width: 32%;">: <b>$studentName</b></td>
                        <td class="label">Kelas</td>
                        <td style="width: 28%;">: <b>Kelas $grade</b></td>
                    </tr>
                    <tr>
                        <td class="label">NISN</td>
                        <td>: $nisn</td>
                        <td class="label">Wali Murid</td>
                        <td>: $parentName</td>
                    </tr>
                </table>

                <div class="section-title">I. LAPORAN AKTIVITAS BELAJAR (LIVE TIME)</div>
                <table class="data-table">
                    <tr>
                        <th style="width: 30%;">Mata Pelajaran</th>
                        <td><b>${report.subject}</b></td>
                    </tr>
                    <tr>
                        <th>Tema / Bab / Materi</th>
                        <td>${report.topicBab}</td>
                    </tr>
                    <tr>
                        <th>Waktu Pelaksanaan</th>
                        <td>Pukul ${report.startTime} s.d. ${report.endTime} WIB (Durasi: <b>${report.durationMinutes} Menit</b>)</td>
                    </tr>
                    <tr>
                        <th>Ringkasan Aktivitas Belajar</th>
                        <td>${report.learningSummary.replace("\n", "<br>")}</td>
                    </tr>
                    <tr>
                        <th>Tingkat Pemahaman Siswa</th>
                        <td><b>${report.understandingLevel}</b></td>
                    </tr>
                    <tr>
                        <th>Catatan / Pendampingan Orang Tua</th>
                        <td><i>"${report.parentFeedback.ifEmpty { "Siswa telah melaksanakan pembelajaran mandiri secara tertib dan didampingi orang tua." }}"</i></td>
                    </tr>
                </table>

                <div class="section-title">II. PEMBIASAAN 7 KEBIASAAN ANAK INDONESIA HEBAT (7 KAIH)</div>
                <ul class="habit-list">
                    <li>1. Bangun Pagi & Merapikan Tempat Tidur: <b>$habit1Icon</b></li>
                    <li>2. Beribadah Sesuai Agama: <b>$habit2Icon</b></li>
                    <li>3. Berolahraga / Aktivitas Fisik Pagi: <b>$habit3Icon</b></li>
                    <li>4. Gerakan Literasi Membaca 15 Menit: <b>$habit4Icon</b></li>
                    <li>5. Makan Makanan Sehat & Bergizi: <b>$habit5Icon</b></li>
                    <li>6. Membantu Orang Tua di Rumah: <b>$habit6Icon</b></li>
                    <li>7. Istirahat & Tidur Malam Tepat Waktu: <b>$habit7Icon</b></li>
                </ul>

                <div class="signature-row">
                    <div class="signature-col">
                        Mengetahui,<br>
                        Orang Tua / Wali Murid
                        <div class="sig-space"></div>
                        <b>( $parentName )</b>
                    </div>
                    <div class="signature-col">
                        Kronggen, $date<br>
                        Guru / Wali Kelas $grade
                        <div class="sig-space"></div>
                        <b>( Siti Rahmawati, S.Pd )</b><br>
                        <small>NIP. 19850412 201001 2 015</small>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateAllReportsHtml(
        context: Context,
        studentName: String,
        grade: Int,
        nisn: String,
        parentName: String,
        reports: List<StudyReportEntity>
    ): String {
        val kopHtml = generateOfficialKopHtml(context)
        val tableRows = StringBuilder()

        reports.forEachIndexed { index, rep ->
            tableRows.append(
                """
                <tr>
                    <td style="text-align: center;">${index + 1}</td>
                    <td>${rep.dateString}<br><span style="font-size: 9pt; color: #555;">${rep.dayName}</span></td>
                    <td><b>${rep.subject}</b><br><span style="font-size: 9.5pt; color: #444;">${rep.topicBab}</span></td>
                    <td>${rep.durationMinutes} m<br><span style="font-size: 9pt; color: #555;">${rep.startTime}-${rep.endTime}</span></td>
                    <td>${rep.learningSummary.replace("\n", "<br>")}</td>
                    <td><b>${rep.understandingLevel}</b></td>
                    <td><i>${rep.parentFeedback}</i></td>
                </tr>
                """.trimIndent()
            )
        }

        val totalMinutes = reports.sumOf { it.durationMinutes }
        val totalHours = totalMinutes / 60
        val remMinutes = totalMinutes % 60

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>Rekapitulasi Laporan Belajar Remote A4 - SD Negeri 4 Kronggen</title>
                <style>
                    @page {
                        size: A4 portrait;
                        margin: 12mm 15mm;
                    }
                    @media print {
                        body { margin: 0; padding: 0; }
                    }
                    body {
                        font-family: 'Times New Roman', Times, serif, 'Segoe UI', Arial, sans-serif;
                        color: #111;
                        font-size: 10.5pt;
                        line-height: 1.35;
                        margin: 10px;
                        background: #fff;
                    }
                    .title-doc {
                        text-align: center;
                        margin-bottom: 12px;
                    }
                    .title-doc h4 {
                        margin: 0;
                        font-size: 12.5pt;
                        font-family: Arial, sans-serif;
                        font-weight: bold;
                        text-decoration: underline;
                        color: #000;
                        text-transform: uppercase;
                    }
                    .info-table { width: 100%; margin-bottom: 12px; border-collapse: collapse; }
                    .info-table td { padding: 3px 5px; font-size: 10pt; vertical-align: top; }
                    .info-table td.label { font-weight: bold; width: 22%; }
                    .report-table { width: 100%; border-collapse: collapse; margin-top: 8px; margin-bottom: 12px; }
                    .report-table th, .report-table td { border: 1px solid #333; padding: 6px 8px; font-size: 9.5pt; }
                    .report-table th { background-color: #072A60; color: #fff; text-align: left; font-family: Arial, sans-serif; }
                    .summary-box { background-color: #F1F5F9; border: 1px solid #CBD5E1; padding: 6px 10px; font-size: 10pt; margin-bottom: 16px; }
                    .signatures { margin-top: 25px; width: 100%; border-collapse: collapse; }
                    .signatures td { width: 50%; text-align: center; font-size: 10pt; vertical-align: top; }
                    .sig-space { height: 55px; }
                </style>
            </head>
            <body>
                $kopHtml

                <div class="title-doc">
                    <h4>REKAPITULASI LAPORAN BELAJAR REMOTE SISWA</h4>
                    <span style="font-family: Arial, sans-serif; font-size: 9pt; color: #444;">Semester Aktif Tahun Ajaran • SD Negeri 4 Kronggen</span>
                </div>

                <table class="info-table">
                    <tr>
                        <td class="label">Nama Siswa</td>
                        <td style="width: 32%;">: <b>$studentName</b></td>
                        <td class="label">Tingkat / Kelas</td>
                        <td style="width: 28%;">: <b>Kelas $grade</b></td>
                    </tr>
                    <tr>
                        <td class="label">NISN</td>
                        <td>: $nisn</td>
                        <td class="label">Wali Murid</td>
                        <td>: $parentName</td>
                    </tr>
                </table>

                <table class="report-table">
                    <thead>
                        <tr>
                            <th style="width: 4%; text-align: center;">No</th>
                            <th style="width: 13%;">Tanggal</th>
                            <th style="width: 20%;">Mata Pelajaran & Materi</th>
                            <th style="width: 12%;">Waktu</th>
                            <th style="width: 26%;">Ringkasan Pembelajaran</th>
                            <th style="width: 11%;">Pemahaman</th>
                            <th style="width: 14%;">Catatan Wali</th>
                        </tr>
                    </thead>
                    <tbody>
                        $tableRows
                    </tbody>
                </table>

                <div class="summary-box">
                    <b>Total Laporan Tercatat:</b> ${reports.size} Sesi Belajar &nbsp;|&nbsp; 
                    <b>Total Akumulasi Waktu Belajar:</b> ${totalHours} Jam ${remMinutes} Menit (${totalMinutes} Menit)
                </div>

                <table class="signatures">
                    <tr>
                        <td>
                            Mengetahui,<br>Orang Tua / Wali Murid
                            <div class="sig-space"></div>
                            <b>( $parentName )</b>
                        </td>
                        <td>
                            Kronggen, ${SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(Date())}<br>
                            Guru / Wali Kelas $grade
                            <div class="sig-space"></div>
                            <b>( Siti Rahmawati, S.Pd )</b><br>
                            <small>NIP. 19850412 201001 2 015</small>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
        """.trimIndent()
    }

    fun printDocument(context: Context, htmlContent: String, jobName: String = "Laporan_Belajar_SDN4_Kronggen") {
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                printManager?.print(
                    jobName,
                    printAdapter,
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setResolution(PrintAttributes.Resolution("id1", "Printing", 300, 300))
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build()
                )
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    fun formatWhatsAppReportText(
        report: StudyReportEntity,
        habit: HabitRecordEntity?,
        student: UserEntity?
    ): String {
        val studentName = student?.fullName ?: report.studentName
        val parentName = student?.parentName ?: report.parentSignatureName.ifEmpty { "Orang Tua Siswa" }
        val grade = report.grade
        val date = report.dateString
        val day = report.dayName

        val habit1 = if (habit?.habit1WakeUp == true) "✅" else "❌"
        val habit2 = if (habit?.habit2Worship == true) "✅" else "❌"
        val habit3 = if (habit?.habit3Exercise == true) "✅" else "❌"
        val habit4 = if (habit?.habit4Literacy == true) "✅ (${habit.literacyBookTitle.ifEmpty { "Buku Literasi" }})" else "❌"
        val habit5 = if (habit?.habit5HealthyFood == true) "✅" else "❌"
        val habit6 = if (habit?.habit6HelpParents == true) "✅" else "❌"
        val habit7 = if (habit?.habit7EarlySleep == true) "✅" else "❌"

        return """
            🏫 *LEMBAR LAPORAN BELAJAR REMOTE (A4 RESMI)*
            *PEMERINTAH KABUPATEN GROBOGAN*
            *DINAS PENDIDIKAN DAN KEBUDAYAAN*
            *SD NEGERI 4 KRONGGEN - TOWARDS LITERATE SCHOOL*
            ====================================
            👤 *Siswa:* $studentName (Kelas $grade)
            🆔 *NISN:* ${student?.nisn ?: "-"}
            📅 *Hari/Tgl:* $day, $date
            👨‍👩‍👧 *Wali Murid:* $parentName
            
            📚 *I. AKTIVITAS BELAJAR (LIVE TIME)*
            • *Mata Pelajaran:* ${report.subject}
            • *Tema/Bab:* ${report.topicBab}
            • *Waktu Belajar:* ${report.startTime} - ${report.endTime} WIB (${report.durationMinutes} menit)
            • *Tingkat Pemahaman:* ${report.understandingLevel}
            • *Ringkasan Belajar:* ${report.learningSummary}
            • *Catatan Wali:* ${report.parentFeedback.ifEmpty { "Didampingi belajar dengan baik." }}
            
            🌟 *II. 7 KEBIASAAN ANAK INDONESIA HEBAT (7KAIH)*
            1. Bangun Pagi: $habit1
            2. Beribadah Tepat Waktu: $habit2
            3. Berolahraga Pagi: $habit3
            4. Gemar Membaca & Literasi: $habit4
            5. Makan Sehat Bergizi: $habit5
            6. Membantu Orang Tua: $habit6
            7. Istirahat Tepat Waktu: $habit7
            
            _Dicetak & Dikirim via Aplikasi Remote Learning SD Negeri 4 Kronggen_
        """.trimIndent()
    }

    fun shareReportViaIntent(context: Context, textContent: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textContent)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Kirim Laporan Belajar SD 4 Kronggen ke...")
        context.startActivity(shareIntent)
    }
}
