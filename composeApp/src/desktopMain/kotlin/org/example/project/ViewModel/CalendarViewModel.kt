package org.example.project.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.project.API.Data.CreateTaskRequest
import org.example.project.API.Data.CreateTaskResponse
import org.example.project.API.Data.DeleteTaskRequest
import org.example.project.API.Data.DeleteTaskResponse
import org.example.project.API.Data.GetMessageListRequest
import org.example.project.API.Data.GetMessageListResponse
import org.example.project.API.Data.GetTaskListResponse
import org.example.project.API.Data.GetUsersListResponse
import org.example.project.API.Data.SendMessageRequest
import org.example.project.API.Data.SendMessageResponse
import org.example.project.API.Data.UpdateTaskRequest
import org.example.project.API.Data.UpdateTaskResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.Message
import org.example.project.Model.MessageWithUser
import org.example.project.Model.Task
import org.example.project.Model.TaskWithID
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import kotlin.concurrent.timer

class CalendarViewModel() : ViewModel(){
    var taskList =  emptyList<TaskWithID>().toMutableStateList()
    var creator = mutableStateOf(User("",""))
    var freeUserList = emptyList<User>().toMutableStateList()
    var createdTask = mutableStateOf(
        Task(
            name = "",
            description = "",
            beginTime = "",
            endTime = "",
            priority = "5",
            percent = "0",
            file = ByteArray(0),
            responsiblePersons = emptyList<User>().toMutableList(),
            creatorUser = User("",""),
            completed = false,
            cycleType = "none",
            cycleDuration = ""
        )
    )
    var sendMessage = mutableStateOf(Message("", ByteArray(0),""))

    var messageList = emptyList<MessageWithUser>().toMutableStateList()

    private var timer: Timer? = null
    private var job: Job? = null
    private var timerM: Timer? = null
    private var jobM: Job? = null

    // Флаг для отслеживания состояния
    private val isSending = mutableStateOf(false)
    private val isSendingM = mutableStateOf(false)

    fun setUser(user: UserSession?){
        if (user != null) {
            creator.value = User(user.login,user.password)
        }
    }
    fun createTask(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.createTask("Bearer $token", CreateTaskRequest(
                accountsDepartment = accountsDepartment, task = createdTask.value
            )
        )

        call.enqueue(object : Callback<CreateTaskResponse> {
            override fun onResponse(call: Call<CreateTaskResponse>, response: Response<CreateTaskResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){

                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<CreateTaskResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun updateTask(user: UserSession?, accountsDepartment: AccountsDepartment, taskWithID: TaskWithID){
        val token = user?.token
        val call = apiService.updateTask("Bearer $token", UpdateTaskRequest(
            accountsDepartment = accountsDepartment, taskWithID = taskWithID
        )
        )

        call.enqueue(object : Callback<UpdateTaskResponse> {
            override fun onResponse(call: Call<UpdateTaskResponse>, response: Response<UpdateTaskResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){

                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<UpdateTaskResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun deleteTask(user: UserSession?, accountsDepartment: AccountsDepartment, taskWithID: TaskWithID){
        val token = user?.token
        val call = apiService.deleteTask(
            "Bearer $token",
            DeleteTaskRequest(
                accountsDepartment,
                taskWithID
            )
        )

        call.enqueue(object : Callback<DeleteTaskResponse> {
            override fun onResponse(call: Call<DeleteTaskResponse>, response: Response<DeleteTaskResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){

                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<DeleteTaskResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }

    fun getUsersList(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.getUsersList("Bearer $token", accountsDepartment)

        call.enqueue(object : Callback<GetUsersListResponse> {
            override fun onResponse(call: Call<GetUsersListResponse>, response: Response<GetUsersListResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            freeUserList.clear()
                            freeUserList.addAll(result.userList)
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetUsersListResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun getTaskList(user: UserSession?, accountsDepartment: AccountsDepartment, userGet: User){
        val token = user?.token
        val call = apiService.getTasks("Bearer $token", userGet, accountsDepartment)

        call.enqueue(object : Callback<GetTaskListResponse> {
            override fun onResponse(call: Call<GetTaskListResponse>, response: Response<GetTaskListResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            taskList.clear()
                            taskList.addAll(result.taskList)
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetTaskListResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun startGettingTaskList(user: UserSession?, accountsDepartment: AccountsDepartment, userGet: User){
        if (isSending.value) return // Уже отправляем

        isSending.value = true
        job = viewModelScope.launch {
            timer = timer(daemon = true, initialDelay = 0, period = 2000) {
                viewModelScope.launch {
                    try {
                        getTaskList(user,accountsDepartment,userGet)
                    } catch (e: Exception) {
                        // Обработка ошибок
                    }
                }
            }
        }
    }
    fun stopSendingRequests() {
        timer?.cancel()
        job?.cancel()
        isSending.value = false
    }

    override fun onCleared() {
        stopSendingRequests()
        super.onCleared()
    }

    fun sendMessageAPI(user: UserSession?, accountsDepartment: AccountsDepartment,
                       taskWithID: TaskWithID){
        val token = user?.token
        val call = apiService.sendMessage("Bearer $token",
            sendMessageRequest = SendMessageRequest(
                accountsDepartment,
                taskWithID,
                message = sendMessage.value
            )
        )

        call.enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(call: Call<SendMessageResponse>, response: Response<SendMessageResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){

                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun getMessageList(user: UserSession?, accountsDepartment: AccountsDepartment,
                       taskWithID: TaskWithID
    ){
        val token = user?.token
        val call = apiService.getMessageList("Bearer $token",
            getMessageListRequest = GetMessageListRequest(
                accountsDepartment = accountsDepartment,
                taskWithID = taskWithID
            )
        )

        call.enqueue(object : Callback<GetMessageListResponse> {
            override fun onResponse(call: Call<GetMessageListResponse>, response: Response<GetMessageListResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            messageList.clear()
                            messageList.addAll(result.messageWUList)
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetMessageListResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun startGettingMessageList(user: UserSession?, accountsDepartment: AccountsDepartment, taskWithID: TaskWithID){
        if (isSendingM.value) return // Уже отправляем

        isSendingM.value = true
        jobM = viewModelScope.launch {
            timerM = timer(daemon = true, initialDelay = 0, period = 2000) {
                viewModelScope.launch {
                    try {
                        getMessageList(user,accountsDepartment,taskWithID)
                    } catch (e: Exception) {
                        // Обработка ошибок
                    }
                }
            }
        }
    }
    fun stopGettingMessages() {
        timerM?.cancel()
        jobM?.cancel()
        isSendingM.value = false
    }
}