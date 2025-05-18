package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.Model.User
import org.example.project.Model.UserSession

import org.example.project.View.EnterScreen
import org.example.project.View.MainUserMenuScreen
import org.example.project.View.RegisterScreen
import org.example.project.View.TaskManagerScreen
import org.example.project.View.UserProfileScreen
import org.example.project.ViewModel.AuthorizeViewModel
import org.example.project.ViewModel.BusinessProcessViewModel
import org.example.project.ViewModel.CalendarViewModel
import org.example.project.ViewModel.HierarchyViewModel
import org.example.project.ViewModel.MainDepartmentViewModel
import org.example.project.ViewModel.ProfileViewModel
import org.example.project.ViewModel.SettingsViewModel
import org.example.project.ViewModel.TaskManagerViewModel


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
                    val authorizeViewModel = remember { AuthorizeViewModel() }
                    val mainDepartmentViewModel = remember { MainDepartmentViewModel() }
                    val profileViewModel = remember { ProfileViewModel()}
                    val taskManagerViewModel = remember { TaskManagerViewModel()}
                    val settingsViewModel = remember { SettingsViewModel()}
                    val hierarchyViewModel = remember { HierarchyViewModel()}
                    val calendarViewModel = remember {CalendarViewModel()}
                    val businessProcessViewModel = remember {BusinessProcessViewModel()}

                    when (currentScreen) {
                        is Screen.Register -> {RegisterScreen(
                            onNavigateToEnter =
                            {
                                currentScreen = Screen.Enter
                            },
                            onRegistration = authorizeViewModel::sendCreateUser,
                            errorText = authorizeViewModel.errorMessage
                        )}
                        is Screen.Enter -> {EnterScreen(
                            onNavigateToRegister = {currentScreen = Screen.Register},
                            onCheckUserAuth = authorizeViewModel::sendLoginUser,
                            onNavigateToMainMenu = {
                                println(authorizeViewModel.user.value.login+" author name in Main")
                                if(authorizeViewModel.user!=null){
                                    currentScreen = Screen.MainUserMenuScreen
                                    mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user.value)
                                }
                            }
                            )}
                        is Screen.MainUserMenuScreen -> {
                            println(authorizeViewModel.user.value.login+" author name in Main main")
                            MainUserMenuScreen(
                                onCreateDepartment = { name:String ->
                                    mainDepartmentViewModel.SendCreateDepartment(
                                        user = authorizeViewModel.user.value,
                                        newName = name
                                    )
                                },
                                departmentList = mainDepartmentViewModel.filteredDepartmentList,
                                onLeaveAccount = {
                                    currentScreen = Screen.Enter
                                    authorizeViewModel.user.value = UserSession("","","")
                                    hierarchyViewModel.lineList.clear()
                                    hierarchyViewModel.postRectangleList.clear()
                                },
                                username = authorizeViewModel.user,
                                onProfileClick = {
                                    currentScreen = Screen.TaskManagerScreen
                                },
                                onGetDepartmentList = {
                                    mainDepartmentViewModel.SendGetDepartmentList(authorizeViewModel.user.value)
                                },
                                onOpenDepartment = {
                                   currentScreen = Screen.TaskManagerScreen
                                },
                                searchString = mainDepartmentViewModel.searchState,
                                filterDepartments = mainDepartmentViewModel::FilterDepartments,
                                selectDepartment = { department ->
                                    taskManagerViewModel.changeSelectedDepartment(department)
                                    calendarViewModel.startGettingTaskList(
                                         user = authorizeViewModel.user.value,
                                         accountsDepartment = department,
                                         userGet = User(authorizeViewModel.user.value.login,"")
                                    )
                                },
                                onEnterDepartment = {
                                        str ->
                                    mainDepartmentViewModel.EnterDepartment(authorizeViewModel.user.value,str)
                                }
                            )
                        }
                        is Screen.TaskManagerScreen->{
                            TaskManagerScreen(
                                onLeaveDepartment = {
                                    currentScreen = Screen.MainUserMenuScreen
                                    calendarViewModel.stopSendingRequests()
                                },
                                currentDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                                userList = taskManagerViewModel.employeeList,
                                onGetUserList = {
                                    taskManagerViewModel.getUsersList(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                inviteUserSetting = settingsViewModel.inviteUserSetting,
                                changeUserSettings = settingsViewModel.changeUserSettings,
                                onGetPermission = {selectedUser: User -> settingsViewModel.getPermission(
                                    authorizeViewModel.user.value, selectedUser,
                                    taskManagerViewModel.selectedAccountsDepartment.value)
                                },
                                createdInviteCode = settingsViewModel.createdInviteCode,
                                onCreateInviteCode = {
                                    settingsViewModel.createInviteCode(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                postRectangleList = hierarchyViewModel.postRectangleList,
                                selectedEmployee = hierarchyViewModel.selectedUser,
                                employeeFreeList = hierarchyViewModel.employeeFreeList,
                                selectedPostRectangle = hierarchyViewModel.selectedPost,
                                onGetUserListHierarchy = {
                                    hierarchyViewModel.getUsersList(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                lineList = hierarchyViewModel.lineList,
                                postRectangleListAPI = hierarchyViewModel.postRectangleAPIList,
                                sendHierarchy =
                                {
                                    hierarchyViewModel.sendHierarchy(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                getHierarchy = {
                                        isArrowed, secondDot, firstDotRectangle ->
                                    hierarchyViewModel.getHierarchy(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value,
                                        isArrowed,secondDot,firstDotRectangle
                                    )
                                },
                                lineAPIList = hierarchyViewModel.lineAPIList,
                                taskList = calendarViewModel.taskList,
                                createdTask = calendarViewModel.createdTask,
                                creatorUser = remember{mutableStateOf(User(authorizeViewModel.user.value.login,""))},
                                setCreator = {
                                    calendarViewModel.setUser(authorizeViewModel.user.value)
                                },
                                createTask = {
                                    calendarViewModel.createTask(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                freeUserList = calendarViewModel.freeUserList,
                                updateFreeUserList = {
                                    calendarViewModel.getUsersList(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                businessProcessList = businessProcessViewModel.businessProcessList,
                                updateTask = {
                                        taskWithID ->
                                    calendarViewModel.updateTask(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value,
                                        taskWithID
                                    )
                                },
                                message = calendarViewModel.sendMessage,
                                messageList = calendarViewModel.messageList,
                                sendMessage = {
                                        taskWithID, str ->
                                    calendarViewModel.sendMessageAPI(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value,
                                        taskWithID
                                    )
                                },
                                startGetMessage = {
                                        taskWithID ->
                                    calendarViewModel.startGettingMessageList(
                                        user = authorizeViewModel.user.value,
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                                        taskWithID = taskWithID
                                    )
                                },
                                stopGetMessage = {calendarViewModel.stopGettingMessages()},
                                bpMutableState = businessProcessViewModel.createdBusinessProcess,
                                sendCreateBusinessProcess = {
                                    businessProcessViewModel.createBusinessProcess(
                                        user = authorizeViewModel.user.value,
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                getBusinessProcess = {
                                    businessProcessViewModel.getBusinessProcessList(
                                        user = authorizeViewModel.user.value,
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                selectedBusinessProcess = businessProcessViewModel.selectedBusinessProcess,
                                updateBusinessProcess = {
                                    businessProcessViewModel.updateBusinessProcess(
                                        user = authorizeViewModel.user.value,
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value
                                    )
                                },
                                runBusinessProcess = {
                                    businessProcessViewModel.runBusinessProcess(
                                        user = authorizeViewModel.user.value,
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                                        postRectangleAPIList = hierarchyViewModel.postRectangleAPIList
                                    )
                                },
                                onDeleteTask = {
                                        taskWithId ->
                                    calendarViewModel.deleteTask(
                                        authorizeViewModel.user.value,
                                        taskManagerViewModel.selectedAccountsDepartment.value,
                                        taskWithId
                                    )
                                },
                                onDeleteBPTask = {
                                        bpTask ->
                                    businessProcessViewModel.deleteBPTask(
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                                        taskBP = bpTask,
                                        user = authorizeViewModel.user.value
                                    )
                                },
                                onDeleteBusinessProcess = {
                                        businessProcess->
                                    businessProcessViewModel.deleteBusinessProcess(
                                        accountsDepartment = taskManagerViewModel.selectedAccountsDepartment.value,
                                        businessProcess = businessProcess,
                                        user = authorizeViewModel.user.value
                                    )
                                }
                            )
                        }
                        is Screen.UserProfile->{
                            profileViewModel.getProfileInfo(authorizeViewModel.user.value)
                            UserProfileScreen(
                                onNavigateBack = {Screen.MainUserMenuScreen},
                                name = profileViewModel.nameState,
                                surname = profileViewModel.surnameState,
                                patronymic = profileViewModel.patronymicState,
                                login = profileViewModel.loginState,
                                phoneNumber = profileViewModel.phoneNumberState,
                                onSaveChanges = {
                                    profileViewModel.postUpdateProfileInfo(user = authorizeViewModel.user.value)
                                },
                                onChangePasswordVM = profileViewModel::updatePassword,
                                userSession = authorizeViewModel.user.value
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
    object TaskManagerScreen: Screen()
    object UserProfile: Screen()
}
