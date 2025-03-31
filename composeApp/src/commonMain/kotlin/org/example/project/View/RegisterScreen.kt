package org.example.project.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch



@Composable
fun RegisterScreen(modifier: Modifier = Modifier, onNavigateToEnter: () -> Unit,
                   onRegistration: (String,String,()->Unit)->Unit ){
    val coroutineScope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        var login = remember{ mutableStateOf("") }
        var password = remember{ mutableStateOf("") }
        var password2 = remember{ mutableStateOf("") }
        TextField(
            value = login.value,
            onValueChange = { newLogin ->
                login.value  = newLogin
            },
            label = {
                Text("Логин")
            }
        )
        TextField(
            value = password.value,
            onValueChange = {newPassword ->
                password.value = newPassword
            },
            label = {
                Text("Пароль")
            },
            modifier = Modifier.padding(top = 40.dp)
        )
        TextField(
            value = password2.value,
            onValueChange = {newPassword ->
                password2.value = newPassword
            },
            label = {
                Text("Повторите пароль")
            },
            modifier = Modifier.padding(top = 40.dp)
        )
        Button(onClick = {
            if(password2.value==password.value) {
                onRegistration(login.value, password.value, onNavigateToEnter)
            }
            else{
            }
        },
            modifier = Modifier.padding(top = 40.dp)
        ){
            Text("Регистрация")
        }
        Button(onClick = {onNavigateToEnter()},
            modifier = Modifier.padding(top = 40.dp)
        ){
            Text("Перейти к входу")
        }
    }
}