package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.User


import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.ViewModel.AuthorizeViewModel
import org.example.project.ViewModel.MainDepartmentViewModel
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
                    val mainDepartmentViewModel = MainDepartmentViewModel()
                    when (currentScreen) {
                        is Screen.Register -> {RegisterScreen(
                            onNavigateToEnter = {currentScreen = Screen.Enter},
                            onRegistration = authorizeViewModel::sendCreateUser,
                            errorText = authorizeViewModel.errorMessage
                        )}
                        is Screen.Enter -> {EnterScreen(
                            onNavigateToRegister = {currentScreen = Screen.Register},
                            onCheckUserAuth = authorizeViewModel::sendLoginUser,
                            onNavigateToMainMenu = {
                                println(authorizeViewModel.user?.login+"Получилось")
                                if(authorizeViewModel.user!=null){
                                    currentScreen = Screen.MainUserMenuScreen
                                }
                            }
                            )}
                        is Screen.MainUserMenuScreen -> {
                            MainUserMenuScreen(
                                onCreateDepartment = { name:String ->
                                    mainDepartmentViewModel.SendCreateDepartment(
                                        user = authorizeViewModel.user,
                                        newName = name
                                    )
                                },
                                departmentList = mainDepartmentViewModel.filteredDepartmentList,
                                onLeaveAccount = {
                                    currentScreen = Screen.Enter
                                    authorizeViewModel.user = null
                                //    hierarchyViewModel.lineList.removeAll(hierarchyViewModel.lineList)
                               //     hierarchyViewModel.postRectangleList.removeAll(hierarchyViewModel.postRectangleList)
                                },
                                username = authorizeViewModel.user?.login ?: "Логин/Почта",
                                onProfileClick = {
                                    //navController.navigate(NavRoutes.UserProfile.route)
                                },
                                onGetDepartmentList = {
                                    mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user)
                                },
                                onOpenDepartment = {
                                   // navController.navigate(NavRoutes.taskManagerScreen.route)
                                },
                                searchString = mainDepartmentViewModel.searchState,
                                filterDepartments = mainDepartmentViewModel::FilterDepartments,
                                selectDepartment = { department ->
                                //    taskManagerViewModel.changeSelectedDepartment(department)
                               //     calendarViewModel.startGettingTaskList(
                               //         user = authorizeViewModel.user,
                               //         accountsDepartment = department,
                                //        userGet = User(authorizeViewModel.user?.login ?: "","")
                               //     )
                                },
                                onEnterDepartment = {
                                        str ->
                                    mainDepartmentViewModel.EnterDepartment(authorizeViewModel.user,str)
                                }
                            )
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
