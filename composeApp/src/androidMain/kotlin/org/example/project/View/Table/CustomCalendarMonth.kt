package org.example.project.View.Table

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun CustomCalendar(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier
) {
    // Получаем календарь для указанного года и месяца
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, 1)
    }

    // Определяем первый день месяца и сколько дней в месяце
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Корректируем день недели (у нас неделя начинается с понедельника)
    val firstDayOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - Calendar.MONDAY

    Column(modifier = modifier.padding(10.dp)) {
        // Строка с днями недели
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Получаем сокращенные названия дней недели
            val daysOfWeek = DayOfWeek.values()
            daysOfWeek.forEach { dayOfWeek ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfWeek.name.take(2),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Сетка календаря (5 строк по 7 дней)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            var dayCounter = 1
            repeat(5) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(7) { column ->
                        val dayIndex = row * 7 + column
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .border(BorderStroke(1.dp,Color.Black))
                            ,
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (dayIndex >= firstDayOffset && dayCounter <= daysInMonth) {
                                Surface(
                                    color = Color.Transparent,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = dayCounter.toString(),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                                dayCounter++
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CalendarPreview() {
    CustomCalendar(
        year = 2025,
        month = 10, // Октябрь
        modifier = Modifier.padding(16.dp)
    )
}