package org.example.project.View


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.UserSession


@Composable
fun MainUserMenuScreen(modifier: Modifier = Modifier,
                       onCreateDepartment :(String) -> Unit,
                       departmentList: SnapshotStateList<AccountsDepartment>,
                       onLeaveAccount: ()->Unit,
                       username:String,
                       onProfileClick: ()->Unit,
                       onGetDepartmentList: ()->Unit,
                       onOpenDepartment: ()-> Unit,
                       searchString: MutableState<String>,
                       filterDepartments: (String)->Unit
){
    var showCard by remember { mutableStateOf(false) }
    Box(){
            Row(
                modifier.fillMaxWidth()
                    .padding(10.dp)
                    .border(1.dp, color = Color.Black),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceAround,
            ){
                Image(imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Иконка профиля",
                    modifier = modifier.padding(top=4.dp, bottom = 4.dp).size(36.dp).clickable(
                        onClick = onProfileClick
                    ))
                Text(text=username,
                    modifier = modifier.padding(top=12.dp, bottom = 10.dp).clickable(
                        onClick = onProfileClick
                    ),
                    fontSize = 16.sp)
                Image(imageVector = Icons.Filled.Close,
                    contentDescription = "Выйти из аккаунта",
                    modifier = modifier.padding(top=10.dp, bottom = 10.dp).clickable(
                        onClick = onLeaveAccount
                    ))
            }
            Column(modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = searchString.value,
                    onValueChange = {
                        newSearch -> searchString.value = newSearch
                        filterDepartments(newSearch)
                                    },
                    label={Text("Поиск")},
                    modifier = modifier.padding(10.dp))
                Text("Список бухгалтерий",
                    modifier = modifier.padding(10.dp),
                    fontWeight = FontWeight(700),
                    fontSize = 20.sp
                )
                DepartmentTable(departmentList = departmentList,
                    onOpenDepartment = onOpenDepartment)
                Button(onClick = {
                    showCard = true
                },
                    modifier = modifier.padding(15.dp)
                ){
                    Text("Создать бухгалтерию")
                }

            }
            if(showCard){
                PopupCard (
                    onDismiss = {showCard = false},
                    onCreateDepartment = onCreateDepartment,
                    onGetDepartmentList = onGetDepartmentList
                )
            }
        }
}
@Composable
fun PopupCard(
    onDismiss: () -> Unit,
    onCreateDepartment: (String) -> Unit,
    onGetDepartmentList: () -> Unit
) {
    val name = remember{ mutableStateOf("") }
    // Затемнение фона
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        // Сама карточка
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp)
                .clickable { } // предотвращает закрытие при клике на карточку
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Стрелка назад"
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().height(600.dp)
            )
            {
                TextField(value = name.value, onValueChange = {newName->name.value=newName}, label= {
                    Text("Название бухгалтерии")
                })
                Button(modifier = Modifier.padding(top = 60.dp), onClick = {
                    onCreateDepartment.invoke(name.value)
                    onDismiss.invoke()
                    onGetDepartmentList()
                }){
                    Text("Создать бухгалтерию")
                }
            }
        }
    }
}

@Composable
fun DepartmentTable(modifier:Modifier=Modifier,
                    departmentList:SnapshotStateList<AccountsDepartment>,
                    onOpenDepartment: ()-> Unit){
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
                DepartmentTableItem(department.name,department.createDate, onOpenDepartment)
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
fun DepartmentTableItem(name:String,time:String, onOpenDepartment: ()-> Unit){
    Row(Modifier.fillMaxWidth().height(100.dp).clickable {
        onOpenDepartment.invoke()
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
                text = "in progress",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}