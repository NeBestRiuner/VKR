package org.example.project.View.Box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import org.example.project.Model.BPTask
import org.example.project.Model.BusinessProcess
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun BusinessProcessCreateBox(
    leaveBPCreate: ()->Unit,
    bpMutableState: MutableState<BusinessProcess>,
    sendCreateBusinessProcess: () -> Unit
) {
    var nameMutableState = remember{
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Row(verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                IconButton(onClick = leaveBPCreate,Modifier.padding(start = 10.dp)){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TextField(
                    value = nameMutableState.value,
                    onValueChange = {
                        newName ->
                            nameMutableState.value = newName
                    },
                    label = {
                        Text(
                            text = "Название бизнес-процесса"
                        )
                    }
                )
                Button(
                    modifier = Modifier.padding(top = 20.dp),
                    onClick = {
                        bpMutableState.value = BusinessProcess(
                            id = 0,
                            name = nameMutableState.value,
                            completed = false,
                            bpTaskList = emptyList<BPTask>().toMutableList()
                        )
                        sendCreateBusinessProcess()
                        leaveBPCreate()
                    }
                ){
                    Text(
                        text = "Создать бизнес процесс"
                    )
                }
            }
        }
    }
}
@Composable
@Preview
fun BusinessProcessCreateBoxPreview(){
    BusinessProcessCreateBox(
        leaveBPCreate = {},
        bpMutableState = remember {
            mutableStateOf(
                BusinessProcess(
                    id = 0,
                    name = "Новый БП",
                    completed = false,
                    bpTaskList = emptyList<BPTask>().toMutableList()
                )
            )
        },
        sendCreateBusinessProcess = {}
    )
}