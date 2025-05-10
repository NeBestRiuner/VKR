package org.example.project.View.Item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.BPTask
import org.example.project.Model.TaskWithID
import org.example.project.Model.User


@Composable
fun BPTaskItemRow(
    modifier: Modifier = Modifier,
    bpTask: BPTask,
    onBPTaskClick: ()->Unit,
    selectedBPTask: MutableState<BPTask>
){
    Row(
        modifier = modifier.padding(10.dp).clickable {
            selectedBPTask.value = bpTask
            onBPTaskClick.invoke()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        // Кружок с числом
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Text(
                text = bpTask.position.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text =  bpTask.name
        )
    }
}

@Preview
@Composable
fun BPTaskItemRowPreview(){
    BPTaskItemRow(
        bpTask =
        BPTask(
            id = 0,
            name = "Задание 1",
            duration = "0",
            priority = "5",
            percent = "0",
            description = "",
            file = ByteArray(0),
            responsibleUser = emptyList<User>().toMutableList(),
            creatorUser = User("",""),
            completed = false,
            position = 1,
            responsiblePost = "Бухгалтеры"
            ),
        onBPTaskClick = {

        },
        selectedBPTask = remember {
            mutableStateOf(
                BPTask(
                    id = 0,
                    name = "Задание 1",
                    duration = "0",
                    priority = "5",
                    percent = "0",
                    description = "",
                    file = ByteArray(0),
                    responsibleUser = emptyList<User>().toMutableList(),
                    creatorUser = User("",""),
                    completed = false,
                    position = 1,
                    responsiblePost = "Бухгалтеры"
                )
            )
        }
    )
}