package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.Model.NavRoutes
import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.View.TaskManagerScreen
import org.example.project.View.UserProfileScreen
import org.example.project.ViewModel.AuthorizeViewModel
import org.example.project.ViewModel.MainDepartmentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authorizeViewModel = AuthorizeViewModel()
            val mainDepartmentViewModel = MainDepartmentViewModel()
            NavHost(navController = navController, startDestination = NavRoutes.Enter.route){
                composable(NavRoutes.Enter.route){
                    EnterScreen(
                        onNavigateToRegister = {navController.navigate(NavRoutes.Register.route)},
                        onCheckUserAuth = authorizeViewModel::sendLoginUser,
                        onNavigateToMainMenu = {
                            if(authorizeViewModel.user!=null){
                                navController.navigate(NavRoutes.MainUserMenu.route)
                                mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user)
                            }
                        }
                    )
                }
                composable(NavRoutes.Register.route){
                    RegisterScreen(
                        onNavigateToEnter = { navController.navigate(NavRoutes.Enter.route) },
                        onRegistration = AuthorizeViewModel()::sendCreateUser)
                }
                composable(NavRoutes.MainUserMenu.route){
                    MainUserMenuScreen(
                        onCreateDepartment = { name:String ->
                            mainDepartmentViewModel.SendCreateDepartment(
                                user = authorizeViewModel.user,
                                newName = name
                            )
                            mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user)
                        },
                        departmentList = mainDepartmentViewModel.filteredDepartmentList,
                        onLeaveAccount = {
                            navController.popBackStack()
                            authorizeViewModel.user = null
                        },
                        username = authorizeViewModel.user?.login ?: "Логин/Почта",
                        onProfileClick = {
                            navController.navigate(NavRoutes.UserProfile.route)
                        },
                        onGetDepartmentList = {
                            mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user)
                        },
                        onOpenDepartment = {
                            navController.navigate(NavRoutes.taskManagerScreen.route)
                        },
                        searchString = mainDepartmentViewModel.searchState,
                        filterDepartments = mainDepartmentViewModel::FilterDepartments
                    )
                }
                composable(NavRoutes.UserProfile.route) {
                    authorizeViewModel.getProfileInfo(authorizeViewModel.user)
                    UserProfileScreen(
                        onNavigateBack = {navController.popBackStack()},
                        name = authorizeViewModel.nameState,
                        surname = authorizeViewModel.surnameState,
                        patronymic = authorizeViewModel.patronymicState,
                        login = authorizeViewModel.loginState,
                        phoneNumber = authorizeViewModel.phoneNumberState,
                        onSaveChanges = {
                            authorizeViewModel.postUpdateProfileInfo(user = authorizeViewModel.user)
                        },
                        onChangePasswordVM = authorizeViewModel::updatePassword
                    )
                }
                composable(NavRoutes.taskManagerScreen.route){
                    TaskManagerScreen(
                        {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

