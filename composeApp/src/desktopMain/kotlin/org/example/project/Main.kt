package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.API.Data.HelloWorldResponse
import org.example.project.API.RetrofitClient.apiService


import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.ViewModel.AuthorizeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Main {
    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            application {
                Window(
                    onCloseRequest = ::exitApplication,
                    title = "VKR",
                ) {
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Enter) }
                    val authorizeViewModel = AuthorizeViewModel()
                    when (currentScreen) {
                        is Screen.Register -> {RegisterScreen(
                            onNavigateToEnter = {currentScreen = Screen.Enter},
                            onRegistration = authorizeViewModel::sendCreateUser
                        )}
                        is Screen.Enter -> {EnterScreen(
                            onNavigateToRegister = {currentScreen = Screen.Register},
                            onCheckUserAuth = authorizeViewModel::sendLoginUser,
                            onNavigateToMainMenu = {
                                if(authorizeViewModel.user!=null){
                                    currentScreen = Screen.MainUserMenuScreen
                                }
                            }
                            )}
                        is Screen.MainUserMenuScreen -> {
                            MainUserMenuScreen()
                        }
                    }
                }
            }

        }
    }
}
sealed class Screen {
    object Enter : Screen()
    object Register : Screen()
    object MainUserMenuScreen: Screen()
}
