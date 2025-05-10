package org.example.project.View.Box

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import org.example.project.Model.BottomNavItem
import org.example.project.Model.BusinessProcess
import org.example.project.View.Table.BusinessProcessTable

@Composable
fun BusinessProcessBox(
    createBusinessProcess: ()->Unit,
    businessProcessList: SnapshotStateList<BusinessProcess>
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
                businessProcessList = businessProcessList
            )
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = createBusinessProcess
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
                name = "Новый БП"
            )
        )
        }
    )
}