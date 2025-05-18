package org.example.project.View

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import org.example.project.API.Data.LineAPI
import org.example.project.API.Data.PostRectangleAPI
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BPTask
import org.example.project.Model.BottomNavItem
import org.example.project.Model.BusinessProcess
import org.example.project.Model.Line
import org.example.project.Model.Message
import org.example.project.Model.MessageWithUser
import org.example.project.Model.PostRectangle
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.View.Box.AnalyticBox
import org.example.project.View.Box.BusinessProcessBox
import org.example.project.View.Box.BusinessProcessCreateBox
import org.example.project.View.Box.BusinessProcessEditBox
import org.example.project.View.Box.HierarchyBox
import org.example.project.View.Box.HomeScreen
import org.example.project.View.Box.SettingsBox

@Composable
fun TaskManagerScreen(
    onLeaveDepartment:()->Unit,
    currentDepartment: AccountsDepartment,
    userList: SnapshotStateList<User>,
    onGetUserList: ()->Unit,
    inviteUserSetting: MutableState<Boolean>,
    changeUserSettings: MutableState<Boolean>,
    onGetPermission: (User) -> Unit,
    createdInviteCode: MutableState<String>,
    onCreateInviteCode: ()->Unit,
    postRectangleList: SnapshotStateList<PostRectangle>,
    selectedEmployee: MutableState<User>,
    employeeFreeList: SnapshotStateList<User>,
    selectedPostRectangle: MutableState<PostRectangle>,
    onGetUserListHierarchy: ()->Unit,
    lineList: SnapshotStateList<Line>,
    postRectangleListAPI: SnapshotStateList<PostRectangleAPI>,
    sendHierarchy: ()->Unit,
    getHierarchy: (isArrowed: MutableState<Boolean>, secondDot: MutableState<Int>,
                   firstDotRectangle: MutableState<PostRectangle>)->Unit,
    lineAPIList: SnapshotStateList<LineAPI>,
    taskList: SnapshotStateList<TaskWithID>,
    createdTask: MutableState<Task>,
    creatorUser: MutableState<User>,
    setCreator: () -> Unit,
    createTask: () -> Unit,
    freeUserList: SnapshotStateList<User>,
    updateFreeUserList: () -> Unit,
    businessProcessList: SnapshotStateList<BusinessProcess>,
    updateTask: (TaskWithID) -> Unit,
    message: MutableState<Message>,
    messageList: SnapshotStateList<MessageWithUser>,
    sendMessage: (TaskWithID, String) -> Unit,
    startGetMessage: (TaskWithID) -> Unit,
    stopGetMessage: () -> Unit,
    bpMutableState: MutableState<BusinessProcess>,
    sendCreateBusinessProcess: () -> Unit,
    getBusinessProcess: () -> Unit,
    selectedBusinessProcess: MutableState<BusinessProcess>,
    updateBusinessProcess: ()->Unit,
    runBusinessProcess: ()->Unit,
    onDeleteTask: (TaskWithID)->Unit,
    onDeleteBPTask: (BPTask) -> Unit,
    onDeleteBusinessProcess: (BusinessProcess)->Unit

){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column{
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().height(60.dp).background(Color(0,75,174))){
                IconButton(onClick = onLeaveDepartment) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Кнопка выхода",
                        tint = Color.White)
                }
                Text(currentDepartment.name, style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                )
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Уведомления",
                        tint = Color.White)
                }
            }
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController = navController, userList, onGetUserList,
                    inviteUserSetting = inviteUserSetting,
                    changeUserSettings = changeUserSettings,
                    onGetPermission = onGetPermission,
                    createdInviteCode = createdInviteCode,
                    onCreateInviteCode = onCreateInviteCode,
                    postRectangleList = postRectangleList,
                    selectedEmployee = selectedEmployee,
                    employeeFreeList = employeeFreeList,
                    selectedPostRectangle = selectedPostRectangle,
                    onGetUserListHierarchy = onGetUserListHierarchy,
                    lineList = lineList,
                    postRectangleListAPI = postRectangleListAPI,
                    sendHierarchy = sendHierarchy,
                    getHierarchy = getHierarchy,
                    lineAPIList = lineAPIList,
                    taskList = taskList,
                    createdTask = createdTask,
                    creatorUser = creatorUser,
                    setCreator = setCreator,
                    createTask = createTask,
                    freeUserList = freeUserList,
                    updateFreeUserList = updateFreeUserList,
                    businessProcessList = businessProcessList,
                    updateTask = updateTask,
                    message = message,
                    messageList = messageList,
                    sendMessage = sendMessage,
                    startGetMessage = startGetMessage,
                    stopGetMessage = stopGetMessage,
                    bpMutableState = bpMutableState,
                    sendCreateBusinessProcess = sendCreateBusinessProcess,
                    getBusinessProcess = getBusinessProcess,
                    selectedBusinessProcess = selectedBusinessProcess,
                    updateBusinessProcess = updateBusinessProcess,
                    runBusinessProcess = runBusinessProcess,
                    onDeleteTask = onDeleteTask,
                    onDeleteBPTask = onDeleteBPTask,
                    onDeleteBusinessProcess = onDeleteBusinessProcess
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Hierarchy,
        BottomNavItem.BusinessProcess,
      //  BottomNavItem.Analytic,
        BottomNavItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color(0,75,174)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                label = { Text(item.title, style = TextStyle(fontSize = 12.sp,
                    textAlign = TextAlign.Center, color = Color.White) ) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Очистка стека навигации при выборе элемента
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, userList: SnapshotStateList<User>,
                    onGetUserList: () -> Unit,
                    inviteUserSetting: MutableState<Boolean>,
                    changeUserSettings: MutableState<Boolean>,
                    onGetPermission: (User)-> Unit,
                    createdInviteCode: MutableState<String>,
                    onCreateInviteCode: ()->Unit,
                    postRectangleList: SnapshotStateList<PostRectangle>,
                    selectedEmployee: MutableState<User>,
                    employeeFreeList: SnapshotStateList<User>,
                    selectedPostRectangle: MutableState<PostRectangle>,
                    onGetUserListHierarchy: () -> Unit,
                    lineList: SnapshotStateList<Line>,
                    postRectangleListAPI: SnapshotStateList<PostRectangleAPI>,
                    sendHierarchy: ()->Unit,
                    getHierarchy: (isArrowed: MutableState<Boolean>, secondDot: MutableState<Int>,
                                   firstDotRectangle: MutableState<PostRectangle>)->Unit,
                    lineAPIList: SnapshotStateList<LineAPI>,
                    taskList: SnapshotStateList<TaskWithID>,
                    createdTask: MutableState<Task>,
                    creatorUser: MutableState<User>,
                    setCreator: () -> Unit,
                    createTask: () -> Unit,
                    freeUserList: SnapshotStateList<User>,
                    updateFreeUserList: () -> Unit,
                    businessProcessList: SnapshotStateList<BusinessProcess>,
                    updateTask: (TaskWithID) -> Unit,
                    message: MutableState<Message>,
                    messageList: SnapshotStateList<MessageWithUser>,
                    sendMessage: (TaskWithID, String) -> Unit,
                    startGetMessage: (TaskWithID) -> Unit,
                    stopGetMessage: () -> Unit,
                    bpMutableState: MutableState<BusinessProcess>,
                    sendCreateBusinessProcess: () -> Unit,
                    getBusinessProcess: () -> Unit,
                    selectedBusinessProcess: MutableState<BusinessProcess>,
                    updateBusinessProcess: ()->Unit,
                    runBusinessProcess: ()->Unit,
                    onDeleteTask: (TaskWithID)->Unit,
                    onDeleteBPTask: (BPTask)->Unit,
                    onDeleteBusinessProcess: (BusinessProcess)->Unit
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Calendar.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(BottomNavItem.Calendar.route) { HomeScreen(
            taskList = taskList,
            createdTask = createdTask,
            creatorUser = creatorUser,
            setCreator = setCreator,
            createTask = createTask,
            freeUserList = freeUserList,
            updateFreeUserList = updateFreeUserList,
            updateTask = updateTask,
            message = message,
            messageList = messageList,
            sendMessage = sendMessage,
            startGetMessage = startGetMessage,
            stopGetMessage = stopGetMessage,
            onDeleteTask = onDeleteTask
        ) }
        composable(BottomNavItem.Hierarchy.route) {
            var isArrowed = remember { mutableStateOf(false) }
            var secondDot = remember { mutableStateOf(0) }
            var firstDotRectangle =  remember { mutableStateOf(PostRectangle()) }
            getHierarchy.invoke(isArrowed,secondDot,firstDotRectangle)
            HierarchyBox(
                postRectangleList = postRectangleList,
                selectedEmployee = selectedEmployee,
                employeeFreeList = employeeFreeList,
                selectedPostRectangle = selectedPostRectangle,
                onGetUserList = onGetUserListHierarchy,
                lineList = lineList,
                postRectangleListAPI = postRectangleListAPI,
                sendHierarchy = sendHierarchy,
                getHierarchy = getHierarchy,
                lineAPIList = lineAPIList,
                isArrowed = isArrowed,
                secondDot = secondDot,
                firstDotRectangle = firstDotRectangle
            )
        }
        composable(BottomNavItem.BusinessProcess.route) {
            var isArrowed = remember { mutableStateOf(false) }
            var secondDot = remember { mutableStateOf(0) }
            var firstDotRectangle =  remember { mutableStateOf(PostRectangle()) }
            getBusinessProcess.invoke()
            getHierarchy.invoke(isArrowed,secondDot,firstDotRectangle)
            BusinessProcessBox(
                createBusinessProcess = {
                    navController.navigate(BottomNavItem.BusinessProcessCreate.route)
                },
                businessProcessList = businessProcessList,
                editBusinessProcess = {
                    navController.navigate(BottomNavItem.BusinessProcessEdit.route)
                },
                selectedBusinessProcess = selectedBusinessProcess,
                runBusinessProcess = runBusinessProcess
            )
        }
        composable(BottomNavItem.Analytic.route) { AnalyticBox() }
        composable(BottomNavItem.Settings.route) {
            onGetUserList.invoke()
            SettingsBox(
                userList = userList,
                onGetUserList = onGetUserList,
                inviteUserSetting = inviteUserSetting,
                changeUserSettings = changeUserSettings,
                onGetPermission = onGetPermission,
                createdInviteCode = createdInviteCode,
                onCreateInviteCode = onCreateInviteCode
            ) }
        composable(BottomNavItem.BusinessProcessCreate.route) {
            BusinessProcessCreateBox(
                leaveBPCreate = {
                    navController.popBackStack()
                },
                bpMutableState = bpMutableState,
                sendCreateBusinessProcess = sendCreateBusinessProcess
            )
        }
        composable(BottomNavItem.BusinessProcessEdit.route){
            BusinessProcessEditBox(
                leaveBPEdit = {
                    navController.popBackStack()
                },
                selectedBusinessProcess = selectedBusinessProcess,
                postRectangleAPIList = postRectangleListAPI,
                businessProcessList = businessProcessList,
                updateBusinessProcess = {
                    navController.popBackStack()
                    updateBusinessProcess.invoke()
                },
                onDeleteBPTask = onDeleteBPTask,
                onDeleteBusinessProcess = onDeleteBusinessProcess
            )
        }

    }
}









@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun TaskManagerScreenProfile(){
    AppTheme {


    TaskManagerScreen(
        {},
        AccountsDepartment(-1,"","","",3),
        emptyList<User>().toMutableStateList(),
        {},
        mutableStateOf(false),
        mutableStateOf(false),
        {},
        mutableStateOf(""),
        {},
        emptyList<PostRectangle>().toMutableStateList(),
        selectedEmployee = mutableStateOf(User("","")),
        employeeFreeList = emptyList<User>().toMutableStateList(),
        selectedPostRectangle = mutableStateOf(PostRectangle(lineList = emptyList<Line>().toMutableStateList())),
        onGetUserListHierarchy = {},
        lineList = emptyList<Line>().toMutableStateList(),
        postRectangleListAPI = emptyList<PostRectangleAPI>().toMutableStateList(),
        sendHierarchy = {},
        getHierarchy = {row,pow,go->},
        lineAPIList = emptyList<LineAPI>().toMutableStateList(),
        taskList = emptyList<TaskWithID>().toMutableStateList(),
        createdTask = mutableStateOf(
            Task(
                name = "",
                description = "",
                beginTime = "",
                endTime = "",
                priority = "",
                percent = "",
                file = ByteArray(0),
                responsiblePersons = emptyList<User>().toMutableStateList(),
                creatorUser = User("",""),
                completed = false,
                cycleType = "none",
                cycleDuration = ""
            )
        ),
        creatorUser = mutableStateOf(
            User("","")
        ),
        setCreator = {},
        createTask = {},
        freeUserList = emptyList<User>().toMutableStateList(),
        updateFreeUserList = {},
        businessProcessList = mutableStateListOf(
            BusinessProcess(
                id = 0,
                name = "Новый БП",
                completed = false,
                bpTaskList = emptyList<BPTask>().toMutableList()
            )
        ),
        updateTask = {taskWithID ->  },
        message = remember { mutableStateOf(Message("",ByteArray(0),"")) },
        messageList = emptyList<MessageWithUser>().toMutableStateList(),
        sendMessage = {taskWithID, str ->  },
        startGetMessage = {taskWithID ->  },
        stopGetMessage = {},
        bpMutableState = remember {
            mutableStateOf(
                BusinessProcess(
                    id = 0,
                    name = "Новый БП",
                    completed = false,
                    bpTaskList = emptyList<BPTask>().toMutableList()
                )
            )
        },
        sendCreateBusinessProcess = {},
        getBusinessProcess = {},
        selectedBusinessProcess = remember {
            mutableStateOf(
                BusinessProcess(
                    id = 0,
                    name = "Новый БП",
                    completed = false,
                    bpTaskList = emptyList<BPTask>().toMutableList()
                )
            )
        },
        updateBusinessProcess = {},
        runBusinessProcess = {},
        onDeleteTask = {},
        onDeleteBPTask = {},
        onDeleteBusinessProcess = {}
    )
    }
}