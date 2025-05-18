package org.example.project.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.GetHierarchyResponse
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
    var lineAPIList = emptyList<LineAPI>().toMutableStateList()

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
                           ){
        postRectangleList.clear()
        lineList.clear()
        for(prAPI in postRectangleAPIList){
            postRectangleList.add(
                PostRectangle(
                    uId = prAPI.uId,
                    position = mutableStateOf(
                        Offset(
                            prAPI.positionX,
                            prAPI.positionY
                        )
                    ),
                    size = DpSize(
                        prAPI.sizeWidth.dp,
                        prAPI.sizeHeight.dp
                    ),
                    text = mutableStateOf(prAPI.text),
                    employeeList = prAPI.employeeList.toMutableStateList(),
                    leaderPostRectangle = null,
                    isArrowed = isArrowed,
                    secondDot = secondDot,
                    firstDotRectangle = firstDotRectangle,
                    centerOffsetX = mutableStateOf(prAPI.centerOffsetX),
                    centerOffsetY = mutableStateOf(prAPI.centerOffsetY),
                    lineList = lineList
                )
            )
        }
        // формирование лидеров postAPIRectangle
        for(postRectangleAPI in postRectangleAPIList){
            if(postRectangleAPI.leaderPostRectangleAPI != null){
                var postRectangleLeader: PostRectangle? = null
                var postRectangleLower: PostRectangle? = null
                for(postRectangle in postRectangleList){
                    if(postRectangleAPI.uId == postRectangle.uId){
                        postRectangleLower = postRectangle
                    }
                    if(postRectangleAPI.leaderPostRectangleAPI!!.uId == postRectangle.uId){
                        postRectangleLeader = postRectangle
                    }
                }
                postRectangleLower!!.leaderPostRectangle = postRectangleLeader
            }
            for(line in lineAPIList){
                var firstPR:PostRectangle? = null
                var secondPR:PostRectangle? = null
                for(pr in postRectangleList){
                    if(pr.uId == line.firstUID){
                        firstPR = pr
                    }
                    if(pr.uId == line.secondUID){
                        secondPR = pr
                    }
                }
                lineList.add(
                    Line(
                        firstPR!!,
                        secondPR!!
                    )
                )
            }
        }

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
    fun getHierarchy(user: UserSession?, accountsDepartment: AccountsDepartment,
                     isArrowed: MutableState<Boolean>, secondDot: MutableState<Int>,
                     firstDotRectangle: MutableState<PostRectangle>,){
        val token = user?.token

        val call = apiService.getHierarchy("Bearer $token",
           accountsDepartment
        )

        call.enqueue(object : Callback<GetHierarchyResponse> {
            override fun onResponse(call: Call<GetHierarchyResponse>, response: Response<GetHierarchyResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            postRectangleAPIList.clear()
                            lineAPIList.clear()
                            if(result.postRectangleAPIList!=null){
                                postRectangleAPIList.addAll(result.postRectangleAPIList)
                                if(result.lineList!=null){
                                    lineAPIList.addAll(result.lineList)
                                    loadPostRectangle(isArrowed,secondDot,firstDotRectangle)
                                }
                            }

                            println(postRectangleAPIList)
                            println(lineAPIList)
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetHierarchyResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
}