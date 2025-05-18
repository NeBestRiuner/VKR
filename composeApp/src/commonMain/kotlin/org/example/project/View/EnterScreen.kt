package org.example.project.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun EnterScreen(modifier: Modifier = Modifier, onNavigateToRegister: () -> Unit,
                onCheckUserAuth:(String,String, ()-> Unit)->Unit,
                onNavigateToMainMenu:()->Unit){
    var login = remember{ mutableStateOf("") }
    var password = remember{ mutableStateOf("") }
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
        Button(onClick = {
                            println(login.value+" Получилось")
                            onCheckUserAuth(login.value,password.value,onNavigateToMainMenu)
                         },
            modifier = Modifier.padding(top = 40.dp),
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color(40,100,206),
                disabledContentColor = Color(0,75,174),
                disabledContainerColor = Color(192,220,253)
            )
        ){
            Text("Войти")
        }
        Button(onClick = onNavigateToRegister,
            modifier = Modifier.padding(top = 40.dp),
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color(40,100,206),
                disabledContentColor = Color(0,75,174),
                disabledContainerColor = Color(192,220,253)
            )
        ){
            Text("Перейти к регистрации")
        }
    }
}