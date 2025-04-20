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
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import org.example.project.View.Card.InviteCodeCard
import java.io.StringBufferInputStream

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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.selectableGroup().fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center){
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked= selectedOption.value == "Department",
                    onCheckedChange={
                        selectedOption.value = "Department"
                    }
                ){
                    Text("Настройки бухгалтерии")
                }
                IconToggleButton(
                    modifier = Modifier.weight(1.0f),
                    checked = selectedOption.value == "Users",
                    onCheckedChange = {
                        selectedOption.value = "Users"
                    }
                ) {
                    Text("Выдача прав")
                }
            }
            if(selectedOption.value == "Department"){
                Box(Modifier.fillMaxSize()){
                    Column(Modifier.padding(10.dp)){

                    }
                    Button(modifier = Modifier.fillMaxWidth().padding(10.dp), onClick = {
                        createCode.value = true
                    }){
                        Text("Сгенерировать ключ для входа")
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
                    onGetUserList.invoke()
                    Column(Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(modifier = Modifier.fillMaxWidth().padding(10.dp) ,
                            text= "Список сотрудников", textAlign = TextAlign.Center)
                        LazyColumn(Modifier.fillMaxSize()){
                            items(userList){
                                user ->
                                    UserItem(user.login, selectedUser, userRight)
                            }
                        }
                    }
                }else{
                    onGetPermission.invoke(User(userRight.value.login,""))
                    Column(Modifier.padding(10.dp)){
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center){
                            IconButton(onClick = {selectedUser.value = false}){
                                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    "Кнопка назад")
                            }
                            Text(userRight.value.login)
                        }
                        Column{
                            RightItem("Право выдавать права",changeUserSettings)
                            RightItem("Право приглашать сотрудников",inviteUserSetting)
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
    },
        horizontalArrangement = Arrangement.Center
    ){
        Text(login)
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

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun SettingsBoxPreview(){
    SettingsBox(
        emptyList<User>().toMutableStateList(),
        {},
        mutableStateOf(false),
        mutableStateOf(false),
        {},
        mutableStateOf(""),
        {}
    )
}