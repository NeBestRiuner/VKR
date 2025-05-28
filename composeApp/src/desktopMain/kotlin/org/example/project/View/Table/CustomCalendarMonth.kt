package org.example.project.View.Table

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.View.Box.parseDate
import org.example.project.View.Item.TaskItemRow
import org.jetbrains.compose.ui.tooling.preview.Preview

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun CustomCalendar(
    year: Int,
    month: Int,
    day: MutableState<Int>,
    modifier: Modifier = Modifier,
    taskList: SnapshotStateList<TaskWithID>,
    selectedData: MutableState<String>,
    selectedUser: MutableState<User>
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
                    Text(
                        text = RusDay.fromInt(dayOfWeek.value).shortName,
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
            modifier = Modifier.fillMaxWidth().weight(1f)
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
                                val curDay = dayCounter
                                val lcalendar = Calendar.getInstance().apply {
                                    set(year,month-1,dayCounter)
                                }

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
                                            modifier = Modifier.padding(4.dp),
                                            color = if (lcalendar.get(Calendar.DAY_OF_WEEK).let {
                                                    it == Calendar.SATURDAY || it == Calendar.SUNDAY
                                                }) Color.Red else Color.Black
                                        )
                                        if(dayTaskCountList[dayCounter-1]>0){
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center){
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.White)
                                                        .border(width = 1.dp,Color.Black,
                                                            CircleShape)
                                                ) {
                                                    Text(
                                                        text = "${dayTaskCountList[dayCounter-1]}",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

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
    }
}

@Composable
@Preview
fun CalendarPreview() {
    CustomCalendar(
        year = 2025,
        month = 10, // Октябрь
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
fun dayHaveTask(taskList: SnapshotStateList<TaskWithID>, dayIndex: Int, year: Int, month:Int):Boolean{
    var have = false
    if(dayIndex>0 && dayIndex<32)
        for (task in taskList){
            val beginDate =  task.beginTime.parseDate()
            val endDate = task.endTime.parseDate()
            val startOfDay = LocalDateTime.of(year, month, dayIndex, 0, 0)
            val endOfDay = LocalDateTime.of(year, month, dayIndex, 23, 59)

            // Проверяем, что beginDate > конца дня И endDate < начала дня
            if(beginDate.isAfter(endOfDay) || endDate.isBefore(startOfDay))
                have = true
        }
    return have
}
fun dayCountTask(taskList: SnapshotStateList<TaskWithID>, dayIndex: Int, year: Int, month:Int,
                 currentUser: MutableState<User>):Int{
    var count = 0

    for (task in taskList){
        val beginDate =  task.beginTime.parseDate()
        val endDate = task.endTime.parseDate()

        val startOfDay = LocalDateTime.of(year, month, dayIndex, 0, 0)
        val endOfDay = LocalDateTime.of(year, month, dayIndex, 23, 59)

        // Проверяем, что beginDate > конца дня И endDate < начала дня
        if(beginDate.isAfter(endOfDay) || endDate.isBefore(startOfDay)){

        }else {
            var employees = false

            for(resp in task.responsiblePersons){
                if(resp.login == currentUser.value.login) employees = true
            }
            if(employees) {

                count++
            }
        }
    }
    return count
}