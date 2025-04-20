package org.example.project.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.GetUsersListResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.Line
import org.example.project.Model.PostRectangle
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HierarchyViewModel: ViewModel() {
    var postRectangleList = emptyList<PostRectangle>().toMutableStateList()
    var employeeFreeList = emptyList<User>().toMutableStateList()
    var selectedUser = mutableStateOf(User("",""))
    var selectedPost = mutableStateOf(PostRectangle())
    var lineList =  emptyList<Line>().toMutableStateList()

    fun getUsersList(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.getUsersList("Bearer $token", accountsDepartment)

        call.enqueue(object : Callback<GetUsersListResponse> {
            override fun onResponse(call: Call<GetUsersListResponse>, response: Response<GetUsersListResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            employeeFreeList.removeAll(employeeFreeList)
                            employeeFreeList.addAll(result.userList)
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