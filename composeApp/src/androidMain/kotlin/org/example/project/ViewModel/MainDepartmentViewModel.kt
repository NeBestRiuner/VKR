package org.example.project.ViewModel

import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDepartmentViewModel: ViewModel() {
    val createdDepartment = AccountsDepartment(-1, "TestDepartment",
        "","Admin")
    var departmentList: MutableList<AccountsDepartment> = mutableListOf()

    fun SendCreateDepartment(user: UserSession?, accountsDepartment: AccountsDepartment,
                             newName: String){
        if(user!=null){
            val token = user.token
            val call = apiService.postCreateDepartment(
                "Bearer $token",
                CreateDepartmentRequest(
                    User(user.login,user.password),
                    accountsDepartment)
            )
            call.enqueue(object : Callback<CreateDepartmentResponse> {
                override fun onResponse(call: Call<CreateDepartmentResponse>, response: Response<CreateDepartmentResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            if(result.status=="success"){
                                println("Success")
                            }
                        }
                        // Обработка успешного ответа
                    } else {
                        // Обработка ошибки
                    }
                }

                override fun onFailure(call: Call<CreateDepartmentResponse>, t: Throwable) {
                    // Обработка ошибки сети
                }
            })
        }
    }
    fun SendGetDepartments(user: UserSession?){
        if(user!=null){
            val token = user.token
            val call = apiService.getDepartments(
                "Bearer $token"
            )
            call.enqueue(object : Callback<CreateDepartmentResponse> {
                override fun onResponse(call: Call<CreateDepartmentResponse>, response: Response<CreateDepartmentResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            if(result.status=="success"){
                                println("Success")
                            }
                        }
                        // Обработка успешного ответа
                    } else {
                        // Обработка ошибки
                    }
                }

                override fun onFailure(call: Call<CreateDepartmentResponse>, t: Throwable) {
                    // Обработка ошибки сети
                }
            })
        }
    }
}