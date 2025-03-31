package org.example.project.ViewModel

import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDepartmentViewModel: ViewModel() {
    val createdDepartment = AccountsDepartment(-1, "TestDepartment",
        "","Admin")

    fun SendCreateDepartment(user: User, accountsDepartment: AccountsDepartment){
        val call = apiService.postCreateDepartment(
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