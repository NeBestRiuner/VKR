package org.example.project.API

import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.GetAllUsersResponse
import org.example.project.API.Data.HelloWorldResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
        @Body
        createDepartmentRequest: CreateDepartmentRequest
    ):Call<CreateDepartmentResponse>
}