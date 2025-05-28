package org.example.project.API

import org.example.project.API.Data.CreateBusinessProcessRequest
import org.example.project.API.Data.CreateBusinessProcessResponse
import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateInviteCodeResponse
import org.example.project.API.Data.CreateTaskRequest
import org.example.project.API.Data.CreateTaskResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.DeleteAccountantRequest
import org.example.project.API.Data.DeleteAccountantResponse
import org.example.project.API.Data.DeleteBPRequest
import org.example.project.API.Data.DeleteBPResponse
import org.example.project.API.Data.DeleteBPTaskRequest
import org.example.project.API.Data.DeleteBPTaskResponse
import org.example.project.API.Data.DeleteTaskRequest
import org.example.project.API.Data.DeleteTaskResponse
import org.example.project.API.Data.EnterDepartmentTokenResponse
import org.example.project.API.Data.GetAllUsersResponse
import org.example.project.API.Data.GetBusinessProcessResponse
import org.example.project.API.Data.GetDepartmentListResponse
import org.example.project.API.Data.GetHierarchyResponse
import org.example.project.API.Data.GetMessageListRequest
import org.example.project.API.Data.GetMessageListResponse
import org.example.project.API.Data.GetPermissionResponse
import org.example.project.API.Data.GetProfileInfoResponse
import org.example.project.API.Data.GetTaskListResponse
import org.example.project.API.Data.GetUsersListResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.API.Data.RunBusinessProcessRequest
import org.example.project.API.Data.RunBusinessProcessResponse
import org.example.project.API.Data.SendHierarchyRequest
import org.example.project.API.Data.SendHierarchyResponse
import org.example.project.API.Data.SendMessageRequest
import org.example.project.API.Data.SendMessageResponse
import org.example.project.API.Data.UpdateBusinessProcessRequest
import org.example.project.API.Data.UpdateBusinessProcessResponse
import org.example.project.API.Data.UpdateProfileInfoRequest
import org.example.project.API.Data.UpdateTaskRequest
import org.example.project.API.Data.UpdateTaskResponse
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {


    @GET("/get-all-users/")
    fun getAllUsers(): Call<GetAllUsersResponse>
    @POST("/create-user/")
    fun postCreateUser(
        @Body user: User
    ):Call<CreateUserResponse>
    @POST("/get-user/")
    fun getLoginUser(
        @Body user: User
    ):Call<LoginUserResponse>
    @POST("/create-accounts-department/")
    fun postCreateDepartment(
        @Header("Authorization") token:String,
        @Body createDepartmentRequest: CreateDepartmentRequest
    ):Call<CreateDepartmentResponse>
    @GET("/get-accounts-departments/")
    fun getDepartmentList(
        @Header("Authorization") token:String,
        @Query("user") user:User
    ):Call<GetDepartmentListResponse>
    @GET("/get-user-profile-info/")
    fun getProfileInfo(
        @Header("Authorization") token:String,
        @Query("user") user:User
    ):Call<GetProfileInfoResponse>
    @POST("/update-user-profile-info/")
    fun postUpdateProfileInfo(
        @Header("Authorization") token:String,
        @Body updateProfileInfoRequest: UpdateProfileInfoRequest
    ):Call<GetProfileInfoResponse>
    @GET("/get-users-list/")
    fun getUsersList(
        @Header("Authorization") token:String,
        @Query("accountDepartment") department: AccountsDepartment
    ):Call<GetUsersListResponse>
    @GET("/get-permission/")
    fun getPermission(
        @Header("Authorization") token:String,
        @Query("user") user: User,
        @Query("accountDepartment") department: AccountsDepartment
    ):Call<GetPermissionResponse>
    @POST("/create-invite-code/")
    fun createInviteCode(
        @Header("Authorization") token:String,
        @Body accountDepartment: AccountsDepartment
        ):Call<CreateInviteCodeResponse>
    @POST("/enter-department/")
    fun enterDepartment(
        @Header("Authorization") token:String,
        @Body enterToken: String
    ):Call<EnterDepartmentTokenResponse>
    @POST("/send-hierarchy/")
    fun sendHierarchy(
        @Header("Authorization") token:String,
        @Body sendHierarchyRequest: SendHierarchyRequest
    ):Call<SendHierarchyResponse>
    @GET("/get-hierarchy/")
    fun getHierarchy(
        @Header("Authorization") token:String,
        @Query("accountDepartment") department: AccountsDepartment
    ):Call<GetHierarchyResponse>

    @POST("/create-task/")
    fun createTask(
        @Header("Authorization") token:String,
        @Body createTaskRequest: CreateTaskRequest
    ):Call<CreateTaskResponse>

    @GET("/get-tasks/")
    fun getTasks(
        @Header("Authorization") token:String,
        @Query("user") user: User,
        @Query("accountDepartment") department: AccountsDepartment
    ):Call<GetTaskListResponse>
    @POST("/update-task/")
    fun updateTask(
        @Header("Authorization") token:String,
        @Body updateTaskRequest: UpdateTaskRequest
    ):Call<UpdateTaskResponse>
    @POST("/send-message/")
    fun sendMessage(
        @Header("Authorization") token:String,
        @Body sendMessageRequest: SendMessageRequest
    ):Call<SendMessageResponse>

    @POST("/get-message-list/")
    fun getMessageList(
        @Header("Authorization") token:String,
        @Body getMessageListRequest: GetMessageListRequest
    ):Call<GetMessageListResponse>

    @POST("/create-business-process/")
    fun sendCreateBusinessProcess(
        @Header("Authorization") token:String,
        @Body createBusinessProcessRequest: CreateBusinessProcessRequest
    ):Call<CreateBusinessProcessResponse>
    @GET("/get-business-process-list/")
    fun getBusinessProcessList(
        @Header("Authorization") token:String,
        @Query("accountDepartment") department: AccountsDepartment
    ):Call<GetBusinessProcessResponse>
    @POST("/update-business-process/")
    fun updateBusinessProcess(
            @Header("Authorization") token: String,
            @Body updateBusinessProcessRequest: UpdateBusinessProcessRequest
            ):Call<UpdateBusinessProcessResponse>
    @POST("/run-business-process/")
    fun runBusinessProcess(
        @Header("Authorization") token: String,
        @Body runBusinessProcessRequest: RunBusinessProcessRequest
    ):Call<RunBusinessProcessResponse>
    @POST("/delete-task/")
    fun deleteTask(
        @Header("Authorization") token: String,
        @Body deleteTaskRequest: DeleteTaskRequest
    ):Call<DeleteTaskResponse>
    @POST("/delete-bptask/")
    fun deleteBPTask(
        @Header("Authorization") token: String,
        @Body deleteBPTaskRequest: DeleteBPTaskRequest
    ):Call<DeleteBPTaskResponse>
    @POST("/delete-business-process/")
    fun deleteBusinessProcess(
        @Header("Authorization") token: String,
        @Body deleteBusinessProcess: DeleteBPRequest
    ):Call<DeleteBPResponse>
    @POST("/delete-accountant/")
    fun deleteAccountant(
        @Header("Authorization") token: String,
        @Body deleteAccountantRequest: DeleteAccountantRequest
    ):Call<DeleteAccountantResponse>
}