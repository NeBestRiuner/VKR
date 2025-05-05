package org.example.project.View.Card

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskCard(
    onDismiss: ()->Unit,
    task: TaskWithID,
    updateTask: () -> Unit,
    sendMessage: (TaskWithID, String) -> Unit
){
    var message = remember { mutableStateOf("") }
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
                .clickable(enabled = false) {   } // предотвращает закрытие при клике на карточку
                .padding(top = 30.dp, bottom = 30.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // Название задачи
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = task.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,

                    ),
                    textAlign = TextAlign.Center
                )
                // Описание
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp, Color.Black, RoundedCornerShape(10.dp)
                        )
                        .fillMaxWidth()
                        .height(100.dp)
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = task.description
                    )
                }
                // Блок с датами
                Row{
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        text = "Дата начала",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,

                            )
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        text = "Дата конца",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,

                            )
                    )
                }
                Row{
                    Text(
                        modifier = Modifier.weight(1f),
                        text = task.beginTime,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = task.endTime,
                        textAlign = TextAlign.Center
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        text = "Приоритет",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                            )
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        text = "Процент выполнения",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                            )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center,

                    ){
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = task.priority,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = task.percent
                    )
                }
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Чат задачи",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp, Color.Black, RoundedCornerShape(10.dp)
                        )
                        .fillMaxWidth()
                        .height(200.dp)
                ){
                    Column {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        )
                        {

                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().
                            background(Color.Black).height(1.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = message.value,
                                onValueChange = {
                                    newMessage -> message.value = newMessage
                                },
                                placeholder = {
                                    Text(
                                        text = "Сообщение"
                                    )
                                }
                            )
                            IconButton(
                                onClick = {
                                    sendMessage(task,message.value)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Кнопка отправить сообщение"
                                )
                            }
                        }
                    }
                }
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = updateTask
                ){
                    Text(
                        text = "Сохранить изменения"
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ShowTaskCardPreview(){
    ShowTaskCard(
        onDismiss = {},
        task = TaskWithID(
            id = 0,
            name = "Разработка нового фича",
            beginTime = "01 Mart 2023 23:00",
            endTime = "15 Mart 2023 11:23",
            priority = "5",
            percent = "0",
            description = "Описание задачи",
            file = ByteArray(0),
            responsiblePersons = emptyList<User>().toMutableList(),
            creatorUser = User("", "")
        ),
        updateTask = {},
        sendMessage = {taskWithID, message ->  }
    )
}
