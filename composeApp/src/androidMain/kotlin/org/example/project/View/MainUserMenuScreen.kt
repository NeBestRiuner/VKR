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
import androidx.compose.material.icons.filled.Refresh
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
import org.example.project.View.Card.EnterDepartmentCard
import org.example.project.View.Card.PopupCard
import org.example.project.View.Table.DepartmentTable


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
                       filterDepartments: (String)->Unit,
                       selectDepartment: (AccountsDepartment)->Unit,
                       onEnterDepartment: (String)->Unit
){
    var showCard by remember { mutableStateOf(false) }
    var enterDepartmentCard by remember{ mutableStateOf(false) }
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
                Row{
                    Text("Список бухгалтерий",
                        modifier = modifier.padding(10.dp),
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp
                    )
                    IconButton(onClick = onGetDepartmentList){
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Обновить список")
                    }
                }
                DepartmentTable(departmentList = departmentList,
                    onOpenDepartment = onOpenDepartment,
                    selectDepartment = selectDepartment
                )
                Row {
                    Button(
                        onClick = {
                            showCard = true
                        },
                        modifier = modifier.padding(15.dp)
                    ) {
                        Text("Создать бухгалтерию")
                    }
                    Button(
                        onClick = {
                            enterDepartmentCard = true
                        },
                        modifier = modifier.padding(15.dp)
                    ) {
                        Text("Войти по коду")
                    }
                }
            }
            if(showCard){
                PopupCard (
                    onDismiss = {showCard = false},
                    onCreateDepartment = onCreateDepartment,
                    onGetDepartmentList = onGetDepartmentList
                )
            }
            if(enterDepartmentCard){
                EnterDepartmentCard(
                    onDismiss = {enterDepartmentCard = false},
                    onEnterDepartment = onEnterDepartment
                )
            }
        }
}