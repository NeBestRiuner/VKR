package org.example.project.View.Table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.BPTask
import org.example.project.Model.BusinessProcess
import org.example.project.View.Item.BusinessProcessItemRow

@Composable
fun BusinessProcessTable(
    modifier: Modifier = Modifier,
    businessProcessList: SnapshotStateList<BusinessProcess>,
    editBusinessProcess: ()->Unit,
    selectedBusinessProcess: MutableState<BusinessProcess>,
    runBusinessProcess: ()->Unit
){
    Column(
        modifier = modifier.padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(color = 0xFF87CEFA)).
            height(50.dp).drawWithContent {
                drawContent()
                val strokeWidth = 1.0f
                val width = size.width
                val height = size.height
                val borderColor = Color.Black
                // Верхняя граница
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f),
                    strokeWidth = strokeWidth
                )

                // Левая граница
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, height),
                    strokeWidth = strokeWidth
                )

                // Правая граница
                drawLine(
                    color = borderColor,
                    start = Offset(width, 0f),
                    end = Offset(width, height),
                    strokeWidth = strokeWidth
                )
                // нижняя граница
                drawLine(
                    color = borderColor,
                    start = Offset(0f, height),
                    end = Offset(width, height),
                    strokeWidth = strokeWidth
                )
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            BusinessProcessTableHeader(
                modifier = Modifier.weight(1f),
                "Название"
            )
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            BusinessProcessTableHeader(
                modifier = Modifier.weight(1f),
                "Редактировать"
            )
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            BusinessProcessTableHeader(
                modifier = Modifier.weight(1f),
                "Запустить"
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(businessProcessList){
                itemBP ->
                    BusinessProcessItemRow(
                        modifier = modifier,
                        businessProcess = itemBP,
                        editBusinessProcess = editBusinessProcess,
                        selectedBusinessProcess = selectedBusinessProcess,
                        runBusinessProcess = runBusinessProcess
                    )
            }
        }
    }

}

@Composable
fun BusinessProcessTableHeader(
    modifier: Modifier,
    name:String
){
    Box(
        modifier = modifier
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun BusinessProcessTablePreview(){
    BusinessProcessTable(
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
        selectedBusinessProcess = remember{ mutableStateOf(
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