package org.example.project.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.Model.UserSession
import org.example.project.View.Card.ChangePasswordCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserProfileScreen(
    onNavigateBack : ()->Unit,
    name: MutableState<String>,
    surname: MutableState<String>,
    patronymic: MutableState<String>,
    login: MutableState<String>,
    phoneNumber: MutableState<String>,
    onSaveChanges: ()->Unit,
    onChangePasswordVM: (String,String,String, UserSession?) -> Unit,
    userSession: UserSession?
){
    var showCard by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()){
        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()){
            IconButton(onClick = onNavigateBack,Modifier.padding(start = 10.dp)){
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
            }
            Image(imageVector = Icons.Rounded.AccountCircle,
                "Фото профиля",
                Modifier
                    .size(180.dp)
                    .padding(end = 10.dp))
        }
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            TextField(
                modifier = Modifier.padding(10.dp),
                value = name.value,
                onValueChange = {
                newName -> name.value = newName
            },
                label = {
                    Text("Имя")
                })
            TextField(
                modifier = Modifier.padding(10.dp),
                value = surname.value,
                onValueChange = {
                newSurname -> surname.value = newSurname
            },
                label = {
                    Text("Фамилия")
                })
            TextField(
                modifier = Modifier.padding(10.dp),
                value = patronymic.value,
                onValueChange = {
                newPatronymic -> patronymic.value = newPatronymic
            },
                label = {
                    Text("Отчество")
                })
            TextField(
                modifier = Modifier.padding(10.dp),
                value = login.value,
                onValueChange = {
                newLogin -> login.value = newLogin
            },
                label = {
                    Text("Логин/Почта")
                })
            TextField(
                modifier = Modifier.padding(10.dp),
                value = phoneNumber.value,
                onValueChange = {
                newPhoneNumber -> phoneNumber.value = newPhoneNumber
            },
                label = {
                    Text("Номер телефона")
                })
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = { showCard = true }){
                Text("Сменить пароль")
            }
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = onSaveChanges){
                Text("Сохранить изменения")
            }
        }
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            ){
            Button(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), onClick = {

            }){
                Text("Удалить аккаунт")
            }
        }

        if(showCard){
            ChangePasswordCard (
                onDismiss = {showCard = false},
                onChangePasswordVM,
                userSession
            )
        }



    }
}


@Composable
@Preview
fun UserProfileScreenPreview(){
    UserProfileScreen(onNavigateBack = {},
        remember {  mutableStateOf("")},
        remember {  mutableStateOf("")},
        remember {  mutableStateOf("")},
        remember {  mutableStateOf("")},
        remember {  mutableStateOf("")},
        {},
        {_,_,_,_ ->},
        userSession = null
        )
}