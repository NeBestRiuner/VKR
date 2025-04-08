package org.example.project.API

import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.GetAllUsersResponse
import org.example.project.API.Data.GetDepartmentListResponse
import org.example.project.API.Data.GetProfileInfoResponse
import org.example.project.API.Data.HelloWorldResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.API.Data.UpdateProfileInfoRequest
import org.example.project.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {

    @GET("/HelloWorld/")
    fun getHelloWorld(): Call<HelloWorldResponse>
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
}