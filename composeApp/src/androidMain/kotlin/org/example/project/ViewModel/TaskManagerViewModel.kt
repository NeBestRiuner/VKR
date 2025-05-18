package org.example.project.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.GetProfileInfoResponse
import org.example.project.API.Data.GetUsersListResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskManagerViewModel: ViewModel() {
    var selectedAccountsDepartment: MutableState<AccountsDepartment> = mutableStateOf(
        AccountsDepartment(-1,"","","",0)
    )
    val employeeList = emptyList<User>().toMutableStateList()





    fun changeSelectedDepartment(accountsDepartment: AccountsDepartment){
        selectedAccountsDepartment.value = accountsDepartment
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
                            employeeList.clear()
                            employeeList.addAll(result.userList)
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


}