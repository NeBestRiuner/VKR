package org.example.project.View.Card

import DateTimePickerButton
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

import org.example.project.Model.User
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import org.example.project.Model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskCard(
    onDismiss: ()->Unit,
    createdTask: MutableState<Task>,
    creatorUser: MutableState<User>,
    setCreator: () -> Unit,
    createTask: () -> Unit,
    freeUserList: SnapshotStateList<User>,
    updateFreeUserList: () -> Unit
){
    val taskName = remember { mutableStateOf("") }
    val selectedUser = remember { mutableStateOf(User("","")) }
    val showAddEmployee = remember { mutableStateOf(false) }
    val selectedCycle = remember { mutableStateOf("No") }
    val taskDescription = remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var priority = remember{ mutableStateOf(5) }
    // Состояние DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.timeInMillis
    )

    val timePickerState = rememberTimePickerState(
        initialHour = selectedDate.get(Calendar.HOUR_OF_DAY),  // Берем часы из selectedDate
        initialMinute = selectedDate.get(Calendar.MINUTE)     // Берем минуты из selectedDate
    )


    // Состояние DatePicker
    val dateEndPickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.timeInMillis
    )

    val timeEndPickerState = rememberTimePickerState(
        initialHour = selectedDate.get(Calendar.HOUR_OF_DAY),  // Берем часы из selectedDate
        initialMinute = selectedDate.get(Calendar.MINUTE)     // Берем минуты из selectedDate
    )

    val createdDateTime by remember {
        derivedStateOf {
            if (datePickerState.selectedDateMillis != null) {
                // Создаем Calendar из выбранной даты
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = datePickerState.selectedDateMillis!!
                    // Устанавливаем выбранное время
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }
                calendar.time // Возвращаем Date
            } else {
                Date() // Текущая дата по умолчанию
            }
        }
    }

    val endDateTime by remember {
        derivedStateOf {
            if (dateEndPickerState.selectedDateMillis != null) {
                // Создаем Calendar из выбранной даты
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = dateEndPickerState.selectedDateMillis!!
                    // Устанавливаем выбранное время
                    set(Calendar.HOUR_OF_DAY, timeEndPickerState.hour)
                    set(Calendar.MINUTE, timeEndPickerState.minute)
                }
                calendar.time // Возвращаем Date
            } else {
                Date() // Текущая дата по умолчанию
            }
        }
    }



    val dateTimeFormat = remember { SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()) }
    val responsibleUserTask = remember {emptyList<User>().toMutableStateList()}
    setCreator.invoke()
    createdTask.value = Task(
        name = taskName.value,
        description = taskDescription.value,
        beginTime = dateTimeFormat.format(createdDateTime),
        endTime = dateTimeFormat.format(endDateTime),
        priority = priority.value.toString(),
        percent = "0",
        file = ByteArray(0),
        responsiblePersons = responsibleUserTask.toMutableList(),
        creatorUser = User(
            login = creatorUser.value.login,
            password = ""
        ),
        completed = false
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        // Сама карточка
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp)
                .clickable(enabled = false) { } // предотвращает закрытие при клике на карточку
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }
                Text(modifier = Modifier.padding().weight(1f),
                    text = "Создание задачи",
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth().height(600.dp)
            ){
                TextField(
                    value = taskName.value,
                    onValueChange = {
                        newName -> taskName.value = newName
                    },
                    label = {
                        Text("Название")
                    },
                    modifier = Modifier.padding(10.dp)
                )
                Text(modifier = Modifier.padding(10.dp),text = "Ответственные")
                AddEmployeeRow(
                    chosenEmployeeList = responsibleUserTask,
                    showAddEmployee = showAddEmployee,
                    updateFreeUserList = updateFreeUserList
                )
                Text(modifier = Modifier.padding(10.dp), text = "Цикличность")
                CycleRow(selectedCycle)
                Text(modifier = Modifier.padding(10.dp), text = "Приоритет")
                PriorityRow(priority = priority)
                Text(modifier = Modifier.padding(10.dp), text = "Время начала и заверешения")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    DateTimePickerButton(
                        datePickerState = datePickerState,
                        timePickerState = timePickerState,
                        selectedDateTime = createdDateTime
                    )
                    DateTimePickerButton(
                        datePickerState = dateEndPickerState,
                        timePickerState = timeEndPickerState,
                        selectedDateTime = endDateTime
                    )
                }
                Text(modifier = Modifier.padding(10.dp), text = "Описание задачи")
                TextField(
                    value = taskDescription.value,
                    onValueChange = {
                        newDescription -> taskDescription.value = newDescription
                    },
                    label = {
                        Text("Описание")
                    },
                    modifier = Modifier.height(150.dp).padding(10.dp)
                )

            }
            // кнопки
            Row(modifier = Modifier.fillMaxWidth()){
                Button(modifier = Modifier.weight(1f).padding(5.dp),
                    onClick = {
                        createTask.invoke()
                        onDismiss.invoke()
                    }
                ){
                    Text(
                        text = "Создать задачу",
                        style = TextStyle(fontSize = 12.sp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(modifier = Modifier.weight(1f).padding(5.dp), onClick = onDismiss){
                    Text(
                        text = "Отменить",
                        style = TextStyle(fontSize = 12.sp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if(showAddEmployee.value){
            AddEmployeeToTaskCard(
                    selectedUser = selectedUser,
                    onDismiss = {showAddEmployee.value = false},
                    employeeFreeList = freeUserList,
                    chooseEmployeeList = responsibleUserTask,
                    taskEmployeeList = createdTask.value.responsiblePersons
            )
        }
    }
}

@Composable
fun AddEmployeeRow(
    chosenEmployeeList: SnapshotStateList<User>,
    showAddEmployee: MutableState<Boolean>,
    updateFreeUserList: () -> Unit){
    Row(Modifier.fillMaxWidth()){
        if (chosenEmployeeList.isNotEmpty()) {
            chosenEmployeeList.forEach { employee ->
                EmployeesAvatar(employee)
            }
        }
        IconButton(onClick = {
            updateFreeUserList.invoke()
            showAddEmployee.value = true
        }){
            Icon(imageVector = Icons.Filled.AddCircle,
                contentDescription = "Кнопка добавить пользователя")
        }
    }
}

@Composable
fun EmployeesAvatar(employee: User){
    Column(Modifier.width(40.dp).padding(horizontal = 2.dp)  ,horizontalAlignment = Alignment.CenterHorizontally){
        Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = "Аватар бухгалтера")
        Text( text = employee.login, style = TextStyle(fontSize = 8.sp))
    }
}

@Composable
fun CycleRow(
    selectedCycle: MutableState<String>
){
    Row(modifier = Modifier.fillMaxWidth()){
        IconToggleButton(
            modifier = Modifier.weight(1.0f),
            checked = selectedCycle.value == "No",
            onCheckedChange = {
                selectedCycle.value = "No"
            }
        ) {
            Text("Нет")
        }
        IconToggleButton(
            modifier = Modifier.weight(1.0f),
            checked = selectedCycle.value == "Weekly",
            onCheckedChange = {
                selectedCycle.value = "Weekly"
            }
        ) {
            Text("Неделя")
        }
        IconToggleButton(
            modifier = Modifier.weight(1.0f),
            checked = selectedCycle.value == "Monthly",
            onCheckedChange = {
                selectedCycle.value = "Monthly"
            }
        ) {
            Text("Месяц")
        }
        IconToggleButton(
            modifier = Modifier.weight(1.0f),
            checked = selectedCycle.value == "Other",
            onCheckedChange = {
                selectedCycle.value = "Other"
            }
        ) {
            Text("Другое")
        }
    }
}

@Composable
fun PriorityRow(priority: MutableState<Int>){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Генерируем кружочки с числами от 1 до 10
        (1..10).forEach { number ->
            Box(
                modifier = Modifier
                    .size(24.dp) // Размер кружка
                    .background(
                        color = if(priority.value != number)Color.White else Color.Blue, // Цвет кружка
                        shape = CircleShape // Делаем круглую форму
                    )
                    .padding(1.dp)
                    .border(1.dp,Color.Black, CircleShape)
                    .clickable
                    {
                        priority.value = number
                    }
                ,
                contentAlignment = Alignment.Center // Выравниваем текст по центру
            ) {
                Text(
                    text = number.toString(),
                    color = Color.Black, // Цвет текста
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun CreateTaskCardPreview(){
    CreateTaskCard(
        onDismiss = {},
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
                creatorUser = User("",""),
                completed = false
            )
        ),
        creatorUser = mutableStateOf(
            User(
                login = "",
                password = ""
            )
        ),
        setCreator = {},
        createTask = {},
        freeUserList = emptyList<User>().toMutableStateList(),
        updateFreeUserList = {}
    )
}