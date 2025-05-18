package org.example.project.View.Box

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.BPTask
import org.example.project.Model.BottomNavItem
import org.example.project.Model.BusinessProcess
import org.example.project.View.Table.BusinessProcessTable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BusinessProcessBox(
    createBusinessProcess: ()->Unit,
    businessProcessList: SnapshotStateList<BusinessProcess>,
    editBusinessProcess: ()->Unit,
    selectedBusinessProcess: MutableState<BusinessProcess>,
    runBusinessProcess: ()->Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Список бизнес-процессов",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            BusinessProcessTable(
                modifier = Modifier.weight(1f),
                businessProcessList = businessProcessList,
                editBusinessProcess = editBusinessProcess,
                selectedBusinessProcess = selectedBusinessProcess,
                runBusinessProcess = runBusinessProcess
            )
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = createBusinessProcess,
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(40,100,206),
                    disabledContentColor = Color(0,75,174),
                    disabledContainerColor = Color(192,220,253)
                )
            ){
                Text(
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    text = "Создать бизнес-процесс"
                )
            }
        }

    }
}

@Composable
@Preview
fun BusinessProcessBoxPreview(){
    BusinessProcessBox(
        createBusinessProcess = {},
        businessProcessList = remember{ mutableStateListOf(
            BusinessProcess(
                id = 0,
                name = "Новый БП",
                completed = false,
                bpTaskList = emptyList<BPTask>().toMutableList()
            )
        )
        },
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