package org.example.project.View.Item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskItemRow(
    taskWithID: TaskWithID,
    showShowTask: MutableState<Boolean>,
    selectedTaskWithID: MutableState<TaskWithID>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp).clickable {
                selectedTaskWithID.value = taskWithID
                showShowTask.value = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Цветная линия
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(100.dp)
                .background(Color.Blue)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Колонка с текстом
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = taskWithID.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = taskWithID.beginTime,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = taskWithID.endTime,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Кружок с числом
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Text(
                text = taskWithID.priority,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Пример использования
@Preview
@Composable
fun TaskItemRowPreview() {
    TaskItemRow(
        taskWithID =
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
                creatorUser = User("",""),
                completed = false
            ),
        showShowTask = remember { mutableStateOf(false) },
        selectedTaskWithID = remember { mutableStateOf(
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
                creatorUser = User("",""),
                completed = false
            )
        ) }

    )
}