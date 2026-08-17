package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.LiteracyGreen
import com.example.ui.theme.SchoolBlueContainer
import com.example.ui.theme.SchoolBluePrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CalendarDayItem(
    val dateString: String,
    val dayOfMonth: Int,
    val dayNameShort: String,
    val dayOfWeekInt: Int, // 1=Senin ... 6=Sabtu, 7=Minggu
    val isToday: Boolean,
    val fullDisplayDate: String
)

@Composable
fun LiveCalendarStrip(
    selectedDate: String,
    selectedDayOfWeek: Int,
    onDateSelected: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysList = remember {
        val list = mutableListOf<CalendarDayItem>()
        val cal = Calendar.getInstance()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDayName = SimpleDateFormat("EEE", Locale("id", "ID"))
        val sdfFull = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        val todayStr = sdfDate.format(Date())

        // Show window: 3 days past, today, and 5 days ahead
        cal.add(Calendar.DAY_OF_YEAR, -3)
        for (i in 0..8) {
            val dateStr = sdfDate.format(cal.time)
            val dayNum = cal.get(Calendar.DAY_OF_MONTH)
            val dayName = sdfDayName.format(cal.time)
            val fullStr = sdfFull.format(cal.time)
            val calDay = cal.get(Calendar.DAY_OF_WEEK)
            val customDayOfWeek = when (calDay) {
                Calendar.MONDAY -> 1
                Calendar.TUESDAY -> 2
                Calendar.WEDNESDAY -> 3
                Calendar.THURSDAY -> 4
                Calendar.FRIDAY -> 5
                Calendar.SATURDAY -> 6
                else -> 1 // Sunday falls back to 1
            }

            list.add(
                CalendarDayItem(
                    dateString = dateStr,
                    dayOfMonth = dayNum,
                    dayNameShort = dayName,
                    dayOfWeekInt = customDayOfWeek,
                    isToday = dateStr == todayStr,
                    fullDisplayDate = fullStr
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Kalender Otomatis",
                        tint = SchoolBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Kalender Pembelajaran Otomatis",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LiteracyGreen.copy(alpha = 0.12f),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(LiteracyGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVE AKTIF",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = LiteracyGreen,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Day Cards Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(daysList) { item ->
                    val isSelected = item.dateString == selectedDate
                    val containerColor = when {
                        isSelected -> SchoolBluePrimary
                        item.isToday -> SchoolBlueContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    val textColor = when {
                        isSelected -> Color.White
                        item.isToday -> SchoolBluePrimary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Column(
                        modifier = Modifier
                            .width(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(containerColor)
                            .clickable {
                                onDateSelected(item.dateString, item.dayOfWeekInt)
                            }
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                            .testTag("cal_day_${item.dateString}"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.dayNameShort.uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected || item.isToday) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = textColor,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${item.dayOfMonth}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (item.isToday) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.White else IndonesianRed)
                            )
                        } else {
                            Spacer(modifier = Modifier.size(6.dp))
                        }
                    }
                }
            }
        }
    }
}
