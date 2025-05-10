package org.example.project.View.Table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.AccountsDepartment


@Composable
fun DepartmentTable(modifier: Modifier = Modifier,
                    departmentList: SnapshotStateList<AccountsDepartment>,
                    onOpenDepartment: ()-> Unit,
                    selectDepartment: (AccountsDepartment)->Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = modifier.background(Color.LightGray).
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
            },
            verticalAlignment = Alignment.CenterVertically
        ){
            DepartmentTableHeader("Название")
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            DepartmentTableHeader("Дата создания")
            VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
            DepartmentTableHeader("Количество сотрудников")
        }
        LazyColumn(modifier = modifier.border(1.dp, color = Color.Black)
            .size(width = 300.dp, height = 400.dp)) {
            items(departmentList){ department ->
                DepartmentTableItem(department.name,department.createDate,
                    department.authorLogin, department.employeesNumber.toString(),
                    onOpenDepartment, selectDepartment)
            }
        }
    }
}
@Composable
fun DepartmentTableHeader(name:String){
    Box(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DepartmentTableItem(name:String,time:String, authorLogin:String,
                        number: String, onOpenDepartment: ()-> Unit,
                        selectDepartment: (AccountsDepartment) -> Unit){
    Row(Modifier.fillMaxWidth().height(100.dp).clickable {
        onOpenDepartment.invoke()
        selectDepartment.invoke(AccountsDepartment(-1,name,time,authorLogin,number.toInt()))
    }){
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        //VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        //VerticalDivider(thickness = 1.dp, color = Color.DarkGray)
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}