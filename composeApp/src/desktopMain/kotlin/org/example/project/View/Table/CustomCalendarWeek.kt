package org.example.project.View.Table


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import org.example.project.Model.Enums.RusDay
import org.example.project.Model.Enums.RusMonth
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.View.Box.isDateInRange
import org.example.project.View.Item.TaskItemRow
import org.jetbrains.compose.ui.tooling.preview.Preview


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
    selectedUser: MutableState<User>,
    selectedTask: MutableState<TaskWithID>,
    showShowTask: MutableState<Boolean>
) {
    val calendar = Calendar.getInstance().apply {
        set(currentYear.value, currentMonth.value - 1, 1)
    }

    // Вычисляем дни для отображения (неделя + соседние дни для плавного скролла)
    val visibleDays = remember(currentMonth.value, currentYear.value) {
        computeVisibleDays(currentYear.value, currentMonth.value, calendar)
    }

    val listState = rememberLazyListState()

    val selectedDay = remember { mutableStateOf(day.value) }
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
                            .background(if (isCurrentMonth){
                                if(dayNumber==selectedDay.value){
                                    Color.LightGray
                                }else{
                                    Color.Transparent
                                }
                            }else Color.LightGray.copy(alpha = 0.3f))
                            .clickable {
                                if (isCurrentMonth) {
                                    day.value = dayNumber
                                    selectedDay.value = dayNumber
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val lcl = Calendar.getInstance().apply {
                                set(currentYear.value,currentMonth.value-1,dayNumber)
                            }
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 14.sp,
                                color = if (isCurrentMonth){
                                    if(lcl.get(Calendar.DAY_OF_WEEK).let {
                                            it == Calendar.SATURDAY || it == Calendar.SUNDAY
                                        })Color.Red else Color.Black
                                }else Color.Gray
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
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(taskList){ task ->
                if(
                    isDateInRange(
                        taskBeginTime = task.beginTime,
                        taskEndTime = task.endTime,
                        year = currentYear.value,
                        month = currentMonth.value,
                        day = day.value
                    )
                ){
                    var employees = false

                    for(resp in task.responsiblePersons){
                        if(resp.login == selectedUser.value.login) employees = true
                    }
                    if(employees) {
                        TaskItemRow(task, showShowTask, selectedTask)
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
                    beginTime = "01 May 2025 23:00",
                    endTime = "15 May 2025 11:23",
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
        selectedUser = remember { mutableStateOf(User("","")) },
        selectedTask = remember { mutableStateOf(
            TaskWithID(
                id = 0,
                name = "Разработка нового фича",
                beginTime = "01 May 2025 23:00",
                endTime = "15 May 2025 11:23",
                priority = "5",
                percent = "0",
                description = "",
                file = ByteArray(0),
                responsiblePersons = emptyList<User>().toMutableList(),
                creatorUser = User("", ""),
                completed = false
        )) },
        showShowTask = remember {
            mutableStateOf(
                false
            )
        }
    )
}
