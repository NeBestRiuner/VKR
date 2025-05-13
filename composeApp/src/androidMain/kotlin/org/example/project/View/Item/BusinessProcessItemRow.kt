package org.example.project.View.Item

import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.Model.BPTask
import org.example.project.Model.BottomNavItem
import org.example.project.Model.BusinessProcess


@Composable
fun BusinessProcessItemRow(
    modifier:Modifier = Modifier,
    businessProcess: BusinessProcess,
    editBusinessProcess: ()->Unit,
    selectedBusinessProcess: MutableState<BusinessProcess>,
    runBusinessProcess: ()->Unit
){
    Row(
        modifier = modifier.
        background(color = Color(0xFFF5FFFA), shape = RoundedCornerShape(2f)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = businessProcess.name,
            textAlign = TextAlign.Center
        )
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                selectedBusinessProcess.value = businessProcess
                editBusinessProcess.invoke()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Изменить БП"
            )
        }
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = runBusinessProcess
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Запустить БП"
            )
        }
    }
}

@Composable
@Preview
fun BusinessProcessItemRowPreview(){
    BusinessProcessItemRow(
        businessProcess = BusinessProcess(
            id = 0,
            name = "Новый БП",
            completed = false,
            bpTaskList = emptyList<BPTask>().toMutableList()
        ),
        editBusinessProcess = {},
        selectedBusinessProcess = remember {
            mutableStateOf(
                BusinessProcess(
                    id = 0,
                    name = "Новый БП",
                    completed = false,
                    bpTaskList = emptyList<BPTask>().toMutableList()
                )
            )
        },
        runBusinessProcess = {}
    )
}