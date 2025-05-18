package org.example.project.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.sun.tools.javac.util.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait


@Composable
fun RegisterScreen(modifier: Modifier = Modifier, onNavigateToEnter: () -> Unit,
                   onRegistration: (String, String, ()->Unit)->Unit,
                   errorText: MutableState<String>
){
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember{ SnackbarHostState() }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var login = remember { mutableStateOf("") }
            var password = remember { mutableStateOf("") }
            var password2 = remember { mutableStateOf("") }
            TextField(
                value = login.value,
                onValueChange = { newLogin ->
                    login.value = newLogin
                },
                label = {
                    Text("Логин")
                }
            )
            TextField(
                value = password.value,
                onValueChange = { newPassword ->
                    password.value = newPassword
                },
                label = {
                    Text("Пароль")
                },
                modifier = Modifier.padding(top = 40.dp)
            )
            TextField(
                value = password2.value,
                onValueChange = { newPassword ->
                    password2.value = newPassword
                },
                label = {
                    Text("Повторите пароль")
                },
                modifier = Modifier.padding(top = 40.dp)
            )
            Button(
                onClick = {
                    if (password2.value == password.value) {
                        onRegistration(login.value, password.value, onNavigateToEnter)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Ошибка, разные пароли",
                                actionLabel = "",
                                duration = SnackbarDuration.Short)
                        }
                    }
                },
                modifier = Modifier.padding(top = 40.dp),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(40,100,206),
                    disabledContentColor = Color(0,75,174),
                    disabledContainerColor = Color(192,220,253)
                )
            ) {
                Text("Регистрация")
            }
            Button(
                onClick = { onNavigateToEnter() },
                modifier = Modifier.padding(top = 40.dp),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(40,100,206),
                    disabledContentColor = Color(0,75,174),
                    disabledContainerColor = Color(192,220,253)
                )
            ) {
                Text("Перейти к входу")
            }
        }
        SnackbarHost(hostState = snackbarHostState,
            modifier = modifier.align(Alignment.BottomCenter))
        LaunchedEffect(errorText.value){
            println(errorText.value+" view")
            when(errorText.value){
                "Ошибка сервера" -> {
                        snackbarHostState.showSnackbar(
                            "Ошибка сервера",
                            actionLabel = "",
                            duration = SnackbarDuration.Short
                        )
                }
                "Ошибка сети" ->{
                    snackbarHostState.showSnackbar(
                        "Ошибка подключения к сети",
                        actionLabel = "",
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {

                }
            }
           // errorText.value = ""
        }
    }
}