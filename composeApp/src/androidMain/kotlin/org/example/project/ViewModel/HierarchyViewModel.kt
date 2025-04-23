package org.example.project.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.GetUsersListResponse
import org.example.project.API.Data.LineAPI
import org.example.project.API.Data.PostRectangleAPI
import org.example.project.API.Data.SendHierarchyRequest
import org.example.project.API.Data.SendHierarchyResponse
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
    var postRectangleAPIList = emptyList<PostRectangleAPI>().toMutableStateList()

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

    fun loadPostRectangle( isArrowed: MutableState<Boolean>, secondDot: MutableState<Int>,
                                  firstDotRectangle: MutableState<PostRectangle>,
                                  lineList: SnapshotStateList<Line>){

    }
    fun sendHierarchy(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val lineListAPI = emptyList<LineAPI>().toMutableList()
        for(line in lineList){
            lineListAPI.add(
                LineAPI(
                    line.firstPostRectangle.uId!!,
                    line.secondPostRectangle.uId!!
                )
            )
        }
        val call = apiService.sendHierarchy("Bearer $token",
            sendHierarchyRequest = SendHierarchyRequest(
                accountsDepartment = accountsDepartment,
                postRectangleAPIList = postRectangleAPIList.toList(),
                lineList = lineListAPI
            )
        )

        call.enqueue(object : Callback<SendHierarchyResponse> {
            override fun onResponse(call: Call<SendHierarchyResponse>, response: Response<SendHierarchyResponse>) {
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

            override fun onFailure(call: Call<SendHierarchyResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
}