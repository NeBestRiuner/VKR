package org.example.project.View.Table

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import org.example.project.Model.Enums.RusDay
import org.example.project.Model.Enums.RusMonth
import org.example.project.Model.TaskWithID
import org.example.project.Model.User


import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun CustomCalendarWeek(
    currentYear: MutableState<Int>,
    currentMonth: MutableState<Int>,
    day: MutableState<Int>,
    modifier: Modifier = Modifier,
    taskList: SnapshotStateList<TaskWithID>,
    selectedData: MutableState<String>,
    selectedUser: MutableState<User>
) {
    /*
    // Получаем календарь для указанного года и месяца
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, 1)
    }

    // Определяем первый день месяца и сколько дней в месяце
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Корректируем день недели (у нас неделя начинается с понедельника)
    val firstDayOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - Calendar.MONDAY

    var dI = 1
    var dayTaskCountList = emptyList<Int>().toMutableStateList()
    while(dI <= daysInMonth){
        dayTaskCountList.add(dayCountTask(taskList,dI,year,month, selectedUser))
        dI++
    }

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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Text(
                            text = RusDay.fromInt(dayOfWeek.value).shortName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Сетка календаря ( по 7 дней)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            var dayCounter = 1
            repeat(1) { row ->
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
                                val curDay = dayCounter
                                Surface(
                                    color = Color.Transparent,
                                    modifier = Modifier.fillMaxSize().clickable {
                                        day.value = curDay
                                        selectedData.value = "Day"
                                    }
                                ) {
                                    Column {
                                        Text(
                                            text = dayCounter.toString(),
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(4.dp)
                                        )

                                    }

                                }
                                dayCounter++
                            }else{
                                Surface(
                                    color = Color.LightGray,
                                    modifier = Modifier.fillMaxSize()
                                ){

                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
    val calendar = Calendar.getInstance().apply {
        set(currentYear.value, currentMonth.value - 1, 1)
    }
   // val currentMonth = remember { mutableStateOf(month.value) }
  //  val currentYear = remember { mutableStateOf(year.value) }

    // Вычисляем дни для отображения (неделя + соседние дни для плавного скролла)
    val visibleDays = remember(currentMonth.value, currentYear.value) {
        computeVisibleDays(currentYear.value, currentMonth.value, calendar)
    }

    val listState = rememberLazyListState()

    // Отслеживаем прокрутку для обновления месяца
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val firstVisibleDate = visibleDays[listState.firstVisibleItemIndex + 3] // Берем центральный день
        calendar.time = firstVisibleDate
        if (calendar.get(Calendar.MONTH) + 1 != currentMonth.value) {
            currentMonth.value = calendar.get(Calendar.MONTH) + 1
            currentYear.value = calendar.get(Calendar.YEAR)
        }
    }

    Column(modifier = modifier.padding(10.dp)) {
        // Название месяца и года
        /*
        Text(
            text = "${RusMonth.fromInt(currentMonth.value).name} ${currentYear.value}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Строка с днями недели
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayOfWeek.values().forEach { dayOfWeek ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Text(
                            text = RusDay.fromInt(dayOfWeek.value).shortName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        */
        Spacer(modifier = Modifier.height(8.dp))

        // Горизонтально прокручиваемая строка с днями
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(visibleDays) { date ->
                calendar.time = date
                val dayNumber = calendar.get(Calendar.DAY_OF_MONTH)
                val dayMonth = calendar.get(Calendar.MONTH) + 1
                val dayYear = calendar.get(Calendar.YEAR)
                var dayWeek = calendar.get(Calendar.DAY_OF_WEEK)-1
                if(dayWeek==0)dayWeek=7

                val isCurrentMonth = dayMonth == currentMonth.value
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(
                        text = RusDay.fromInt(dayWeek).shortName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(1.dp, if (isCurrentMonth) Color.Black else Color.LightGray)
                            .background(if (isCurrentMonth) Color.Transparent else Color.LightGray.copy(alpha = 0.3f))
                            .clickable {
                                if (isCurrentMonth) {
                                    day.value = dayNumber
                                    selectedData.value = "Day"
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 14.sp,
                                color = if (isCurrentMonth) Color.Black else Color.Gray
                            )
                            // Можно добавить количество задач для этого дня
                            val taskCount = dayCountTask(taskList, dayNumber, dayYear, dayMonth, selectedUser)
                            if (taskCount > 0) {
                                Text(
                                    text = taskCount.toString(),
                                    fontSize = 10.sp,
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

private fun computeVisibleDays(year: Int, month: Int, calendar: Calendar): List<Date> {
    val days = mutableListOf<Date>()

    // Добавляем несколько дней из предыдущего месяца
    calendar.set(year, month - 1, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysFromPrevMonth = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - Calendar.MONDAY

    calendar.add(Calendar.DAY_OF_MONTH, -daysFromPrevMonth)
    repeat(daysFromPrevMonth) {
        days.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Добавляем дни текущего месяца
    calendar.set(year, month - 1, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    repeat(daysInMonth) {
        days.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Добавляем дни следующего месяца до завершения недели
    val daysFromNextMonth = (7 - (days.size % 7)) % 7
    repeat(daysFromNextMonth) {
        days.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Добавляем еще недели для плавного скролла
    repeat(7 * 2) { // 2 дополнительные недели
        days.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return days
}


@SuppressLint("RememberReturnType")
@Composable
@Preview
fun CalendarWeekPreview() {
    CustomCalendarWeek(
        currentYear = remember { mutableStateOf(2025) },
        currentMonth = remember { mutableStateOf(2) }, // Октябрь
        day = remember {  mutableStateOf(3)},
        modifier = Modifier.padding(16.dp),
        taskList = remember {
            mutableStateListOf(
                TaskWithID(
                    id = 0,
                    name = "Разработка нового фича",
                    beginTime = "01 Mart 2023 23:00",
                    endTime = "15 Mart 2023 11:23",
                    priority = "5",
                    percent = "0",
                    description = "",
                    file = ByteArray(0),
                    responsiblePersons = emptyList<User>().toMutableList(),
                    creatorUser = User("", ""),
                    completed = false
                )
            )
        },
        selectedData = remember { mutableStateOf("Month") },
        selectedUser = remember { mutableStateOf(User("","")) }
    )
}
