package org.example.project.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateDepartmentRequest
import org.example.project.API.Data.CreateDepartmentResponse
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.EnterDepartmentTokenResponse
import org.example.project.API.Data.GetDepartmentListResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDepartmentViewModel: ViewModel() {
    var createdDepartment = AccountsDepartment(-1, "TestDepartment",
        "","Admin",1)
    var departmentList = emptyList<AccountsDepartment>().toMutableStateList()
    var searchState = mutableStateOf("")
    var filteredDepartmentList = emptyList<AccountsDepartment>().toMutableStateList()

    fun SendCreateDepartment(user: UserSession?, newName: String){
        createdDepartment = AccountsDepartment(createdDepartment.id,newName,
            createdDepartment.createDate, user?.login ?: "", employeesNumber = 1
        )
        if(user!=null){
            val token = user.token
            val call = apiService.postCreateDepartment(
                "Bearer $token",
                CreateDepartmentRequest(
                    User(user.login,""),
                    createdDepartment)
            )
            call.enqueue(object : Callback<CreateDepartmentResponse> {
                override fun onResponse(call: Call<CreateDepartmentResponse>, response: Response<CreateDepartmentResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            if(result.status=="200"){
                                println("status: 200")
                                SendGetDepartmentList(user)
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
    fun SendGetDepartmentList(user: UserSession?){
        if(user!=null){
            val token = user.token
            val call = apiService.getDepartmentList(
                "Bearer $token",
                User(user.login,"")
            )
            call.enqueue(object : Callback<GetDepartmentListResponse> {
                override fun onResponse(call: Call<GetDepartmentListResponse>, response: Response<GetDepartmentListResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            if(result.status=="200"){
                                departmentList.removeAll(departmentList)
                                departmentList.addAll(result.departmentList)
                                FilterDepartments("")
                            }
                        }
                        // Обработка успешного ответа
                    } else {
                        // Обработка ошибки
                    }
                }

                override fun onFailure(call: Call<GetDepartmentListResponse>, t: Throwable) {
                    // Обработка ошибки сети
                }
            })
        }
    }
    fun FilterDepartments(newSearch: String){
        if(newSearch == ""){
            filteredDepartmentList.removeAll(filteredDepartmentList)
            filteredDepartmentList.addAll(departmentList)
        }else{
            filteredDepartmentList.removeAll(filteredDepartmentList)
            filteredDepartmentList.addAll(
                departmentList.filter {
                                                department ->
                                            department.name.contains(newSearch, ignoreCase = true) ||
                                                    department.createDate.contains(newSearch, ignoreCase = true)
                                        }
            )
        }
    }
    fun EnterDepartment(user: UserSession?, enterToken: String){
        if(user!=null){
            val token = user.token
            val call = apiService.enterDepartment(
                "Bearer $token", enterToken
            )
            call.enqueue(object : Callback<EnterDepartmentTokenResponse> {
                override fun onResponse(call: Call<EnterDepartmentTokenResponse>, response: Response<EnterDepartmentTokenResponse>) {
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

                override fun onFailure(call: Call<EnterDepartmentTokenResponse>, t: Throwable) {
                    // Обработка ошибки сети
                }
            })
        }
    }
}