package org.example.project.View


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.AccountsDepartment


@Composable
fun MainUserMenuScreen(modifier: Modifier = Modifier,
                       onCreateDepartment :(String) -> Unit,
                       departmentList: MutableList<AccountsDepartment>){
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
                    modifier = modifier.padding(top=10.dp, bottom = 10.dp))
                Text(text="Логин/Почта",
                    modifier = modifier.padding(top=12.dp, bottom = 10.dp),
                    fontSize = 16.sp)
                Image(imageVector = Icons.Filled.Close,
                    contentDescription = "Выйти из аккаунта",
                    modifier = modifier.padding(top=10.dp, bottom = 10.dp))
            }
            Column(modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                TextField(value = "",
                    onValueChange = {},
                    label={Text("Поиск")},
                    modifier = modifier.padding(10.dp))
                Text("Список бухгалтерий",
                    modifier = modifier.padding(10.dp),
                    fontWeight = FontWeight(700),
                    fontSize = 20.sp
                )
                DepartmentTable(departmentList = departmentList)
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
                    onCreateDepartment = onCreateDepartment
                )
            }
        }
}
@Composable
fun PopupCard(
    onDismiss: () -> Unit,
    onCreateDepartment: (String) -> Unit
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
                Button(modifier = Modifier.padding(top = 60.dp), onClick = {onCreateDepartment.invoke(name.value)}){
                    Text("Создать бухгалтерию")
                }
            }
        }
    }
}

@Composable
fun DepartmentTable(modifier:Modifier=Modifier,
                    departmentList:MutableList<AccountsDepartment>){
    Column(
        modifier.fillMaxSize()
    ){
        Row(
            modifier = modifier.background(Color.LightGray)
        ){
            DepartmentTableHeader("Название")
            DepartmentTableHeader("Дата создания")
            DepartmentTableHeader("Количество сотрудников")
        }
        LazyColumn(modifier = modifier.border(1.dp, color = Color.Black)
            .size(width = 300.dp, height = 500.dp)) {
            items(departmentList){ department ->
                DepartmentTableItem(department.name,department.createDate)
            }
        }
    }
    Column(
        modifier=modifier.verticalScroll(rememberScrollState())
    ){

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
            fontSize = 16.sp
        )
    }
}

@Composable
fun DepartmentTableItem(name:String,time:String){
    Row(Modifier.fillMaxWidth()){
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Box(
            modifier = Modifier
                .width(100.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "in progress",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}