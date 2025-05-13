package org.example.project.View.Box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.API.Data.PostRectangleAPI
import org.example.project.Model.BPTask
import org.example.project.Model.BusinessProcess
import org.example.project.Model.User
import org.example.project.View.Card.ShowEditBPTaskCard
import org.example.project.View.Item.AddBPTaskItem
import org.example.project.View.Item.BPTaskItemRow
import org.example.project.View.Item.BusinessProcessItemRow
import org.example.project.View.Item.FinalEventItem
import org.example.project.View.Item.StartEventItem

@Composable
fun BusinessProcessEditBox(
    leaveBPEdit: ()->Unit,
    selectedBusinessProcess: MutableState<BusinessProcess>,
    postRectangleAPIList: SnapshotStateList<PostRectangleAPI>,
    businessProcessList: SnapshotStateList<BusinessProcess>,
    updateBusinessProcess: ()->Unit
){
    var bpStateTaskList = remember { selectedBusinessProcess.value.bpTaskList.sortedBy { it.position }.toMutableStateList() }
    var showBPTaskEditCard = remember{ mutableStateOf(false) }
    var selectedBPTask = remember {
        mutableStateOf(
            BPTask(
                id = 0,
                name = "Задание",
                duration = "1",
                priority = "5",
                percent = "0",
                description = "Описание",
                file = ByteArray(0),
                responsibleUser = emptyList<User>().toMutableList(),
                creatorUser = User("",""),
                completed = false,
                position = bpStateTaskList.size+1,
                responsiblePost = ""
            )
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                IconButton(onClick = leaveBPEdit, Modifier.padding(start = 10.dp)){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
                }
            }
            Text(
                text = "Структура бизнес-процесса",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                StartEventItem()
                LazyColumn(
                    Modifier.weight(1f)
                ) {
                    items(bpStateTaskList){
                        bptask ->
                            BPTaskItemRow(
                                modifier = Modifier.width(250.dp),
                                bpTask = bptask,
                                onBPTaskClick = {
                                    showBPTaskEditCard.value = true
                                },
                                selectedBPTask = selectedBPTask
                            )
                    }
                }
                AddBPTaskItem(
                    onItemAdd = {
                        selectedBusinessProcess.value.bpTaskList.add(
                                    BPTask(
                                        id = 0,
                                        name = "Задание",
                                        duration = "1",
                                        priority = "5",
                                        percent = "0",
                                        description = "Описание",
                                        file = ByteArray(0),
                                        responsibleUser = emptyList<User>().toMutableList(),
                                        creatorUser = User("",""),
                                        completed = false,
                                        position = selectedBusinessProcess.value.bpTaskList.size+1,
                                        responsiblePost = ""
                                    )
                        )
                        bpStateTaskList.add(
                            BPTask(
                                id = 0,
                                name = "Задание",
                                duration = "1",
                                priority = "5",
                                percent = "0",
                                description = "Описание",
                                file = ByteArray(0),
                                responsibleUser = emptyList<User>().toMutableList(),
                                creatorUser = User("",""),
                                completed = false,
                                position = bpStateTaskList.size+1,
                                responsiblePost = ""
                            )
                        )
                    }
                )
                FinalEventItem()
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        selectedBusinessProcess.value = BusinessProcess(
                            id = selectedBusinessProcess.value.id,
                            name = selectedBusinessProcess.value.name,
                            completed = selectedBusinessProcess.value.completed,
                            bpTaskList = bpStateTaskList.toMutableList()
                        )
                        for(bp in businessProcessList){
                            if(selectedBusinessProcess.value.id == bp.id){
                                bp.bpTaskList.clear()
                                bp.bpTaskList.addAll(bpStateTaskList)
                            }
                        }
                        updateBusinessProcess.invoke()
                    }
                ){
                    Text("Сохранить изменения")
                }
            }
        }
        if(showBPTaskEditCard.value){
            ShowEditBPTaskCard(
                selectedBPTask = selectedBPTask,
                showEditBPTaskCard = showBPTaskEditCard,
                bpStateTaskList = bpStateTaskList,
                postRectangleAPIList = postRectangleAPIList
            )
        }
    }
}

@Preview
@Composable
fun BusinessProcessEditBoxPreview(){
    BusinessProcessEditBox(
        leaveBPEdit = {},
        selectedBusinessProcess = remember {
            mutableStateOf(
                BusinessProcess(
                    id = 0,
                    name = "",
                    completed = false,
                    bpTaskList =
                        mutableListOf(
                            BPTask(
                                id = 0,
                                name = "Задание",
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
                )
            )
        },
        postRectangleAPIList = emptyList<PostRectangleAPI>().toMutableStateList(),
        businessProcessList = emptyList<BusinessProcess>().toMutableStateList(),
        updateBusinessProcess = {}
    )
}