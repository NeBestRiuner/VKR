package org.example.project.View.Card

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import org.example.project.API.Data.PostRectangleAPI
import org.example.project.Model.BPTask
import org.example.project.Model.PostRectangle
import org.example.project.Model.User
import org.example.project.View.Button.BPTaskEditComboBoxButton


@Composable
fun ShowEditBPTaskCard(
    selectedBPTask: MutableState<BPTask>,
    showEditBPTaskCard: MutableState<Boolean>,
    bpStateTaskList: SnapshotStateList<BPTask>,
    postRectangleAPIList: SnapshotStateList<PostRectangleAPI>,
){
    var priority = remember{ mutableStateOf(selectedBPTask.value.priority.toInt()) }
    var name = remember { mutableStateOf(selectedBPTask.value.name) }
    var description = remember { mutableStateOf(selectedBPTask.value.description) }
    var durationS = remember { mutableStateOf(selectedBPTask.value.duration) }
    // Затемнение фона
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = {
                showEditBPTaskCard.value = false
            })
    ) {
        // Сама карточка
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp)
                .clickable(enabled = false) { } // предотвращает закрытие при клике на карточку
                .padding(top=30.dp, bottom= 30.dp)
        ) {
            var editName = remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                IconButton(onClick = {
                    showEditBPTaskCard.value = false
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Задание с кружком
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Кружок с числом
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)

                    ) {
                            Text(
                                text = selectedBPTask.value.position.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                    }
                if(!editName.value){
                    Text(
                        modifier = Modifier.padding(start = 10.dp)
                            .clickable { editName.value = true },
                        text = selectedBPTask.value.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }else{
                    TextField(
                        modifier = Modifier.padding(start = 10.dp).onKeyEvent { event ->
                            if (event.key == Key.Enter) {
                                editName.value = false
                                true
                            } else {
                                false
                            }
                        },
                        value = name.value,
                        onValueChange = {
                                newName ->
                            name.value = newName
                        }
                    )
                }

                }

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
                    TextField(
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)
                            .height(90.dp),
                        value = description.value,
                        onValueChange = {
                            newText ->
                                description.value = newText
                        },
                    )
                }
                // приоритет
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(modifier = Modifier.padding(10.dp), text = "Приоритет", style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ))
                    PriorityRow(priority = priority)
                }
                // должность
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(modifier = Modifier.padding(10.dp), text = "Должность исполнителей", style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ))
                    BPTaskEditComboBoxButton(
                        bpTask = selectedBPTask.value,
                        postRectangleAPIList = postRectangleAPIList
                    )
                }
                // Длительность
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(modifier = Modifier.padding(10.dp), text = "Длительность задачи", style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ))
                    TextField(
                        value = durationS.value,
                        onValueChange = {
                            newDuration ->
                                durationS.value = newDuration
                        },
                        label = {
                            Text(
                                text = "Часы",
                                style = TextStyle(fontSize = 14.sp)
                            )
                        }
                    )
                }
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        for(bpTask in bpStateTaskList){
                            if(bpTask.position == selectedBPTask.value.position){
                                selectedBPTask.value.description  = description.value
                                bpTask.description = selectedBPTask.value.description
                                bpTask.responsiblePost = selectedBPTask.value.responsiblePost
                                bpTask.responsibleUser = selectedBPTask.value.responsibleUser
                                selectedBPTask.value.priority = priority.value.toString()
                                bpTask.priority = selectedBPTask.value.priority
                                bpTask.file = selectedBPTask.value.file
                                selectedBPTask.value.name = name.value
                                bpTask.name = selectedBPTask.value.name
                                selectedBPTask.value.duration = durationS.value
                                bpTask.duration = selectedBPTask.value.duration
                                for(post in postRectangleAPIList){
                                    if(bpTask.responsiblePost==post.text){
                                        bpTask.responsibleUser = post.employeeList.toMutableList()
                                    }
                                }
                            }
                        }

                        showEditBPTaskCard.value = false
                    },
                    colors = ButtonColors(
                        contentColor = Color.White,
                        containerColor = Color(40,100,206),
                        disabledContentColor = Color(0,75,174),
                        disabledContainerColor = Color(192,220,253)
                    )
                ) {
                    Text(text = "Сохранить изменения")
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun ShowEditBPTaskCardPreview(){
    ShowEditBPTaskCard(
        showEditBPTaskCard = remember {
            mutableStateOf(true)
        },
        selectedBPTask = remember {
            mutableStateOf(
                BPTask(
                    id = 0,
                    name = "Задание",
                    duration = "0",
                    priority = "5",
                    percent = "0",
                    description = "Описание",
                    file = ByteArray(0),
                    responsibleUser = emptyList<User>().toMutableList(),
                    creatorUser = User("",""),
                    completed = false,
                    position = 1,
                    responsiblePost = "Бухгалтеры"
                )
            )
        },
        bpStateTaskList = remember { emptyList<BPTask>().toMutableStateList() },
        postRectangleAPIList = emptyList<PostRectangleAPI>().toMutableStateList()
    )
}