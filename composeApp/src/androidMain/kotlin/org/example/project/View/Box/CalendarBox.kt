package org.example.project.View.Box

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BottomNavItem
import org.example.project.Model.Enums.RusMonth
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.View.Card.CreateTaskCard
import org.example.project.View.Table.CustomCalendar
import org.example.project.View.Table.DepartmentTableItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


// Экранные компоненты
@Composable
fun HomeScreen(
    taskList: SnapshotStateList<TaskWithID>,
    createdTask : MutableState<Task>,
    creatorUser: MutableState<User>,
    setCreator: () -> Unit,
    createTask: () -> Unit,
    freeUserList: SnapshotStateList<User>,
    updateFreeUserList: () -> Unit
) {
    val selectedType = remember{ mutableStateOf("Calendar") }
    val selectedData = remember{ mutableStateOf("Month") }
    val showCreateTask = remember{ mutableStateOf(false) }
    val month = remember { mutableStateOf( Calendar.getInstance().get(Calendar.MONTH) + 1) }
    val year = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val day = remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Row(modifier = Modifier.fillMaxWidth()){
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked= selectedType.value == "Calendar",
                    onCheckedChange={
                        selectedType.value = "Calendar"
                    }
                ){
                    Text("Календарь")
                }
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked = selectedType.value == "List",
                    onCheckedChange = {
                        selectedType.value = "List"
                    }
                ) {
                    Text("Список")
                }
            }
            if(selectedType.value == "Calendar"){
                Row(modifier = Modifier.fillMaxWidth()){
                    IconToggleButton(
                        modifier = Modifier.weight(1.0f),
                        checked = selectedData.value == "Month",
                        onCheckedChange = {
                            selectedData.value = "Month"
                        }
                    ) {
                        Text("Месяц")
                    }
                    IconToggleButton(
                        modifier = Modifier.weight(1.0f),
                        checked= selectedData.value == "Week",
                        onCheckedChange={
                            selectedData.value = "Week"
                        }
                    ){
                        Text("Неделя")
                    }
                    IconToggleButton(
                        modifier = Modifier.weight(1.0f),
                        checked = selectedData.value == "Day",
                        onCheckedChange = {
                            selectedData.value = "Day"
                        }
                    ) {
                        Text("День")
                    }
                }
                Text("${RusMonth.fromInt(month.value).russianName}, 2025", modifier = Modifier.padding(10.dp))
                if(selectedData.value == "Month"){
                    CustomCalendar(year = year.value,month = month.value)
                }
                if(selectedData.value == "Week"){

                }
                if(selectedData.value == "Day"){
                    Text(modifier = Modifier.padding(10.dp),text = "${day.value} ${RusMonth.fromInt(month.value).russianName}")
                    LazyColumn(modifier = Modifier.border(1.dp, color = Color.Black)
                        .weight(1f)) {
                        items(taskList){ task ->
                            if(isDateInRange(
                                taskBeginTime = task.beginTime,
                                taskEndTime = task.endTime,
                                year = year.value,
                                month = month.value,
                                day = day.value
                            )){
                                TaskItemRow(task)
                            }
                        }
                    }
                }
            }
            else{
                LazyColumn(modifier = Modifier.border(1.dp, color = Color.Black)
                    .weight(1f)) {
                    items(taskList){ task ->
                        TaskItemRow(task)
                    }
                }
            }
            Button(onClick = {
                showCreateTask.value = true
            }){
                Text("Создать задачу")
            }
        }
        if(showCreateTask.value){
            CreateTaskCard(
                onDismiss = {
                    showCreateTask.value  = false
                },
                createdTask = createdTask,
                creatorUser = creatorUser,
                setCreator = setCreator,
                createTask = createTask,
                freeUserList = freeUserList,
                updateFreeUserList = updateFreeUserList
            )
        }
    }
}

@Composable
fun TaskItemRow(task: TaskWithID){
    Row(modifier = Modifier.fillMaxWidth()){
        Text(text = task.name)
        Text(text = task.beginTime)
        Text(text = task.endTime)
        Text(text = task.priority)
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        emptyList<TaskWithID>().toMutableStateList(),
        createdTask = mutableStateOf(
            Task(
                name = "",
                description = "",
                beginTime = "",
                endTime = "",
                priority = "",
                percent = "",
                file = ByteArray(0),
                responsiblePersons = emptyList<User>().toMutableStateList(),
                creatorUser = User("","")
            )
        ),
        creatorUser = mutableStateOf(
            User("","")
        ),
        setCreator = {},
        createTask = {},
        freeUserList = emptyList<User>().toMutableStateList(),
        updateFreeUserList = {}
    )
}
// Сначала преобразуем строковые даты в удобный для сравнения формат
fun String.parseDate(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH)
    return LocalDateTime.parse(this, formatter)
}

// Функция для сравнения дат
fun isDateInRange(taskBeginTime: String, taskEndTime: String,
                  year: Int, month: Int, day: Int): Boolean {
    return try {
        val beginDate = taskBeginTime.parseDate()
        val endDate = taskEndTime.parseDate()

        val startOfDay = LocalDateTime.of(year, month, day, 0, 0)
        val endOfDay = LocalDateTime.of(year, month, day, 23, 59)

        // Проверяем, что beginDate > конца дня И endDate < начала дня
        beginDate.isAfter(endOfDay) && endDate.isBefore(startOfDay)
    } catch (e: Exception) {
        false // В случае ошибки парсинга
    }
}