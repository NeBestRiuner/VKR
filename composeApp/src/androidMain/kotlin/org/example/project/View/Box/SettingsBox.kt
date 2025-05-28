package org.example.project.View.Box

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import org.example.project.View.Card.InviteCodeCard

@Composable
fun SettingsBox(
    userList: SnapshotStateList<User>,
    onGetUserList: ()->Unit,
    inviteUserSetting: MutableState<Boolean>,
    changeUserSettings: MutableState<Boolean>,
    onGetPermission: (User)->Unit,
    createdInviteCode: MutableState<String>,
    onCreateInviteCode: ()->Unit
) {
    val selectedOption = remember{ mutableStateOf("Department") }
    val selectedUser = remember{
        mutableStateOf(false)
    }
    val userRight = remember {
        mutableStateOf(User("",""))
    }
    val createCode = remember { mutableStateOf(false) }
    val editHierarchy = remember { mutableStateOf(false) }
    val createBP = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.selectableGroup().fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center){
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked= selectedOption.value == "Department",
                    onCheckedChange={
                        onGetUserList.invoke()
                        selectedOption.value = "Department"
                    }
                ){
                    Text("Приглашение сотрудников")
                }
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked = selectedOption.value == "Users",
                    onCheckedChange = {
                        onGetUserList.invoke()
                        selectedOption.value = "Users"
                    }
                ) {
                    Text("Выдача прав")
                }
            }
            if(selectedOption.value == "Department"){
                Box(Modifier.fillMaxSize()){
                    Column {
                        Text(modifier = Modifier.fillMaxWidth().padding(10.dp) ,
                            text= "Список сотрудников", textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ))
                        LazyColumn(Modifier.weight(1f)){
                            items(userList){
                                    user ->
                                UserItem(user.login, selectedUser, userRight)
                            }
                        }
                        Button(modifier = Modifier.fillMaxWidth().padding(10.dp), onClick = {
                            createCode.value = true
                        },
                            colors = ButtonColors(
                                contentColor = Color.White,
                                containerColor = Color(40,100,206),
                                disabledContentColor = Color(0,75,174),
                                disabledContainerColor = Color(192,220,253)
                            ),
                            )
                        {
                            Text(
                                text = "Сгенерировать ключ для входа",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                            )
                        }
                    }
                    if(createCode.value){
                        onCreateInviteCode.invoke()
                        InviteCodeCard(
                            createdCode = createdInviteCode,
                            onDismiss = {
                                createCode.value = false
                            }
                        )
                    }
                }
            }else{
                if(!selectedUser.value){

                    Column(Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(modifier = Modifier.fillMaxWidth().padding(10.dp) ,
                            text= "Список сотрудников", textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        LazyColumn(Modifier.fillMaxSize()){
                            items(userList){
                                user ->
                                    UserRightItem(user.login, selectedUser, userRight,
                                        Modifier.padding(10.dp),{onGetPermission.invoke(User(userRight.value.login,""))})
                            }
                        }
                    }
                }else{

                    Column(Modifier.padding(10.dp)){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start){
                            IconButton(onClick = {selectedUser.value = false}){
                                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    "Кнопка назад")
                            }
                            Text(
                                modifier = Modifier.weight(1f),
                                text = userRight.value.login,
                                style = TextStyle(textAlign = TextAlign.Center)
                            )
                        }
                        Column{
                            RightItem("Право выдавать права",changeUserSettings)
                            RightItem("Право приглашать и выгонять сотрудников",inviteUserSetting)
                            RightItem("Право редактировать иерархию",editHierarchy)
                            RightItem("Право создавать бизнес-процессы",createBP)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(login: String, selectedUser: MutableState<Boolean>, userRight: MutableState<User>){
    Row(Modifier.fillMaxWidth().clickable {
        selectedUser.value = true
        userRight.value = User(login, "")
    }.padding(start = 100.dp, end = 100.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "Аватарка пользователя"
        )
        Text(
            modifier = Modifier.padding(start = 10.dp).weight(1f),
            text = login,
            textAlign = TextAlign.Start
        )
        IconButton(onClick = {

        }){
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Выгнать пользователя")
        }
    }
}

@Composable
fun RightItem(rightName: String, checked: MutableState<Boolean>){
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Text(rightName, Modifier.weight(0.75f))
        Checkbox(checked = checked.value,
            onCheckedChange = {
                                checked.value = it
                              },
            Modifier.weight(0.25f))
    }
}

@Composable
fun UserRightItem(login: String, selectedUser: MutableState<Boolean>, userRight: MutableState<User>,
                  modifier: Modifier, sendRequestRight:()->Unit){
    Row(modifier.fillMaxWidth().clickable {
        selectedUser.value = true
        userRight.value = User(login, "")
        sendRequestRight.invoke()
    }.padding(start = 100.dp, end = 100.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "Аватарка пользователя"
        )
        Text(
            modifier = Modifier.padding(start = 10.dp).weight(1f),
            text = login,
            textAlign = TextAlign.Start
        )
    }
}
@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun SettingsBoxPreview(){
    SettingsBox(
        mutableStateListOf(
            User("Admin","")
        ),
        {},
        mutableStateOf(false),
        mutableStateOf(false),
        {},
        mutableStateOf(""),
        {}
    )
}