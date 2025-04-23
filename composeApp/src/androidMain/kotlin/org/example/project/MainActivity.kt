package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.API.Data.EnterDepartmentTokenResponse
import org.example.project.Model.NavRoutes
import org.example.project.Model.User
import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.View.TaskManagerScreen
import org.example.project.View.UserProfileScreen
import org.example.project.ViewModel.AuthorizeViewModel
import org.example.project.ViewModel.HierarchyViewModel
import org.example.project.ViewModel.MainDepartmentViewModel
import org.example.project.ViewModel.ProfileViewModel
import org.example.project.ViewModel.SettingsViewModel
import org.example.project.ViewModel.TaskManagerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authorizeViewModel = AuthorizeViewModel()
            val mainDepartmentViewModel = MainDepartmentViewModel()
            val profileViewModel = ProfileViewModel()
            val taskManagerViewModel = TaskManagerViewModel()
            val settingsViewModel = SettingsViewModel()
            val hierarchyViewModel = HierarchyViewModel()
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
                        onRegistration = AuthorizeViewModel()::sendCreateUser,
                        errorText = authorizeViewModel.errorMessage
                    )

                }
                composable(NavRoutes.MainUserMenu.route){
                    MainUserMenuScreen(
                        onCreateDepartment = { name:String ->
                            mainDepartmentViewModel.SendCreateDepartment(
                                user = authorizeViewModel.user,
                                newName = name
                            )
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
                        filterDepartments = mainDepartmentViewModel::FilterDepartments,
                        selectDepartment = taskManagerViewModel::changeSelectedDepartment,
                        onEnterDepartment = {
                            str ->
                            mainDepartmentViewModel.EnterDepartment(authorizeViewModel.user,str)
                        }
                    )
                }
                composable(NavRoutes.UserProfile.route) {
                    profileViewModel.getProfileInfo(authorizeViewModel.user)
                    UserProfileScreen(
                        onNavigateBack = {navController.popBackStack()},
                        name = profileViewModel.nameState,
                        surname = profileViewModel.surnameState,
                        patronymic = profileViewModel.patronymicState,
                        login = profileViewModel.loginState,
                        phoneNumber = profileViewModel.phoneNumberState,
                        onSaveChanges = {
                            profileViewModel.postUpdateProfileInfo(user = authorizeViewModel.user)
                        },
                        onChangePasswordVM = profileViewModel::updatePassword,
                        userSession = authorizeViewModel.user
                    )
                }
                composable(NavRoutes.taskManagerScreen.route){
                    TaskManagerScreen(
                        onLeaveDepartment = {navController.popBackStack()},
                        currentDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                        userList = taskManagerViewModel.employeeList,
                        onGetUserList = {
                            taskManagerViewModel.getUsersList(
                                authorizeViewModel.user,
                                taskManagerViewModel.selectedAccountsDepartment.value
                            )
                        },
                        inviteUserSetting = settingsViewModel.inviteUserSetting,
                        changeUserSettings = settingsViewModel.changeUserSettings,
                        onGetPermission = {selectedUser: User -> settingsViewModel.getPermission(
                            authorizeViewModel.user!!, selectedUser,
                            taskManagerViewModel.selectedAccountsDepartment.value)
                        },
                        createdInviteCode = settingsViewModel.createdInviteCode,
                        onCreateInviteCode = {
                            settingsViewModel.createInviteCode(
                                authorizeViewModel.user,
                                taskManagerViewModel.selectedAccountsDepartment.value
                            )
                        },
                        postRectangleList = hierarchyViewModel.postRectangleList,
                        selectedEmployee = hierarchyViewModel.selectedUser,
                        employeeFreeList = hierarchyViewModel.employeeFreeList,
                        selectedPostRectangle = hierarchyViewModel.selectedPost,
                        onGetUserListHierarchy = {
                            hierarchyViewModel.getUsersList(
                                authorizeViewModel.user,
                                taskManagerViewModel.selectedAccountsDepartment.value
                            )
                        },
                        lineList = hierarchyViewModel.lineList,
                        postRectangleListAPI = hierarchyViewModel.postRectangleAPIList,
                        sendHierarchy =
                        {
                            hierarchyViewModel.sendHierarchy(
                                authorizeViewModel.user,
                                taskManagerViewModel.selectedAccountsDepartment.value
                            )
                        }
                    )
                }
            }
        }
    }
}

