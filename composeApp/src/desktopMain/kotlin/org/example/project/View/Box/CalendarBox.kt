package org.example.project.View.Box


import androidx.annotation.RequiresApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.internal.toImmutableList
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BottomNavItem
import org.example.project.Model.Enums.RusMonth
import org.example.project.Model.Message
import org.example.project.Model.MessageWithUser
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.View.Card.CreateTaskCard
import org.example.project.View.Card.ShowTaskCard
import org.example.project.View.Item.TaskItemRow
import org.example.project.View.Table.CustomCalendar
import org.example.project.View.Table.CustomCalendarWeek
import org.example.project.View.Table.DepartmentTableItem
import org.jetbrains.compose.ui.tooling.preview.Preview
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
    updateFreeUserList: () -> Unit,
    updateTask: (TaskWithID) -> Unit,
    message: MutableState<Message>,
    messageList: SnapshotStateList<MessageWithUser>,
    sendMessage: (TaskWithID, String) -> Unit,
    startGetMessage: (TaskWithID) -> Unit,
    stopGetMessage: () -> Unit,
    onDeleteTask: (TaskWithID)->Unit
) {
    val selectedType = remember{ mutableStateOf("Calendar") }
    val selectedData = remember{ mutableStateOf("Month") }
    val showCreateTask = remember{ mutableStateOf(false) }
    val showShowTask = remember { mutableStateOf(false) }
    val month = remember { mutableStateOf( Calendar.getInstance().get(Calendar.MONTH) + 1) }
    val year = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val day = remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    val currentUser = remember { mutableStateOf(creatorUser.value) }
    val selectedTask = remember { mutableStateOf(TaskWithID(
        0,"","","","","","", ByteArray(0),
        emptyList<User>().toMutableList(),User("",""),false
    )) }

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
                // Уровень с названием месяца, года и текущем пользователем
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        if(selectedData.value=="Month"||selectedData.value=="Week"){
                            IconButton(
                                modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                                onClick = {
                                    if(month.value==1){
                                        year.value--
                                        month.value=12
                                    }else{
                                        month.value--
                                    }
                                }
                            ){ Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Кнопка выбрать прошлый месяц"
                            ) }
                        }
                        Text("${RusMonth.fromInt(month.value).russianName}, ${year.value}", modifier = Modifier.padding(10.dp)
                            , textAlign = TextAlign.Center)
                        if(selectedData.value=="Month"||selectedData.value=="Week")
                        IconButton(
                            modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                            onClick = {
                                if(month.value==12){
                                    year.value++
                                    month.value=1
                                }else{
                                    month.value++
                                }
                            }
                        ){ Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Кнопка выбрать следующий месяц"
                        ) }
                    }
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center){
                        Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = "Иконка бухгалтера")
                        Text(text = currentUser.value.login, modifier = Modifier.padding(start = 10.dp))
                    }
                }

                if(selectedData.value == "Month"){
                    CustomCalendar(year = year.value,month = month.value, day = day,
                        taskList = taskList, selectedData = selectedData, selectedUser = currentUser)
                }
                if(selectedData.value == "Week"){
                    CustomCalendarWeek(currentYear = year,currentMonth = month, day = day,
                        taskList = taskList, selectedData = selectedData, selectedUser = currentUser,
                        selectedTask = selectedTask, showShowTask = showShowTask, modifier = Modifier.weight(1f))
                }
                if(selectedData.value == "Day"){
                    Row{
                        IconButton(
                            modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                            onClick = {
                                if(day.value == 1){
                                    if(month.value==1){
                                        year.value--
                                        month.value=12
                                    }else{
                                        month.value--
                                    }
                                    day.value = Calendar.getInstance().apply {
                                        set(year.value, month.value, 1)
                                    }.getActualMaximum(Calendar.DAY_OF_MONTH)
                                }else{
                                    day.value --
                                }
                            }
                        ){ Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Кнопка выбрать прошлый месяц"
                        ) }
                        Text(modifier = Modifier.padding(10.dp),text = "${day.value} ${RusMonth.fromInt(month.value).russianName}")
                        IconButton(
                            modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                            onClick = {
                                var maxDays = Calendar.getInstance().apply {
                                    set(year.value, month.value, 1)
                                }.getActualMaximum(Calendar.DAY_OF_MONTH)
                                if(day.value==maxDays){
                                    if(month.value==12){
                                        year.value++
                                        month.value=1
                                    }else{
                                        month.value++
                                    }
                                    day.value=1
                                }else{
                                    day.value++
                                }

                            }
                        ){ Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Кнопка выбрать прошлый месяц"
                        ) }
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(taskList){ task ->
                            if(
                                isDateInRange(
                                        taskBeginTime = task.beginTime,
                                        taskEndTime = task.endTime,
                                        year = year.value,
                                        month = month.value,
                                        day = day.value
                                )
                            ){
                                var employees = false

                                for(resp in task.responsiblePersons){
                                    if(resp.login == currentUser.value.login) employees = true
                                }
                                if(employees) {
                                    TaskItemRow(task, showShowTask, selectedTask)
                                }

                            }
                        }
                    }
                }
            }
            else{
                LazyColumn(modifier = Modifier.border(1.dp, color = Color.Black)
                    .weight(1f)) {
                    items(taskList){ task ->
                        /*var employees = false

                        for(resp in task.responsiblePersons){
                         if(resp.login == currentUser.value.login) employees = true
                        }
                        if(employees) {*/
                            TaskItemRow(task, showShowTask, selectedTask)
                        //}
                    }
                }
            }
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                showCreateTask.value = true
            },
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(40,100,206),
                    disabledContentColor = Color(0,75,174),
                    disabledContainerColor = Color(192,220,253)
                )
            ){
                Text(
                    text = "Создать задачу",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
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
        if(showShowTask.value){
            startGetMessage(selectedTask.value)
            ShowTaskCard(
                onDismiss = {
                    showShowTask.value = false
                    stopGetMessage()
                },
                task = selectedTask.value,
                updateTask = updateTask,
                sendMessage = sendMessage,
                accountUser = creatorUser,
                message = message,
                messageList = messageList,
                onDeleteTask = onDeleteTask
            )
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        emptyList<TaskWithID>().toMutableStateList(),
        createdTask = mutableStateOf(
            Task(
                name = "",
                description = "",
                beginTime = "01 May 2025 23:00",
                endTime = "02 May 2025 11:23",
                priority = "",
                percent = "",
                file = ByteArray(0),
                responsiblePersons = emptyList<User>().toMutableStateList(),
                creatorUser = User("",""),
                completed = false,
                cycleType = "none",
                cycleDuration = ""
            )
        ),
        creatorUser = mutableStateOf(
            User("Admin","")
        ),
        setCreator = {},
        createTask = {},
        freeUserList = emptyList<User>().toMutableStateList(),
        updateFreeUserList = {},
        updateTask = {taskWithID ->  },
        message = remember { mutableStateOf(Message("", ByteArray(0),"")) },
        messageList = emptyList<MessageWithUser>().toMutableStateList(),
        sendMessage = {taskWithID, str ->  },
        startGetMessage = {taskWithID -> },
        stopGetMessage = {},
        onDeleteTask = {}
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
        !(beginDate.isAfter(endOfDay) || endDate.isBefore(startOfDay))
    } catch (e: Exception) {
        false // В случае ошибки парсинга
    }
}