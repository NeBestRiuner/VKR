package org.example.project.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import org.example.project.API.Data.CreateBusinessProcessRequest
import org.example.project.API.Data.CreateBusinessProcessResponse
import org.example.project.API.Data.GetBusinessProcessResponse
import org.example.project.API.Data.PostRectangleAPI
import org.example.project.API.Data.RunBusinessProcessRequest
import org.example.project.API.Data.RunBusinessProcessResponse
import org.example.project.API.Data.SendMessageRequest
import org.example.project.API.Data.SendMessageResponse
import org.example.project.API.Data.UpdateBusinessProcessRequest
import org.example.project.API.Data.UpdateBusinessProcessResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.BPTask
import org.example.project.Model.BusinessProcess
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessProcessViewModel {
    var businessProcessList = emptyList<BusinessProcess>().toMutableStateList()
    var createdBusinessProcess = mutableStateOf(
        BusinessProcess(
            id = 0,
            name = "",
            completed = false,
            bpTaskList = emptyList<BPTask>().toMutableList()
        )
    )
    val selectedBusinessProcess = mutableStateOf(
        BusinessProcess(
            id = 0,
            name = "",
            completed = false,
            bpTaskList = emptyList<BPTask>().toMutableList()
        )
    )
    fun createBusinessProcess(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.sendCreateBusinessProcess("Bearer $token",
            CreateBusinessProcessRequest(
                accountsDepartment = accountsDepartment,
                businessProcess = createdBusinessProcess.value
            )
        )

        call.enqueue(object : Callback<CreateBusinessProcessResponse> {
            override fun onResponse(call: Call<CreateBusinessProcessResponse>, response: Response<CreateBusinessProcessResponse>) {
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

            override fun onFailure(call: Call<CreateBusinessProcessResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun getBusinessProcessList(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.getBusinessProcessList("Bearer $token",
            accountsDepartment
        )

        call.enqueue(object : Callback<GetBusinessProcessResponse> {
            override fun onResponse(call: Call<GetBusinessProcessResponse>, response: Response<GetBusinessProcessResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            businessProcessList.removeAll(
                                businessProcessList.toList()
                            )
                            businessProcessList.addAll(
                                result.businessProcessList
                            )
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetBusinessProcessResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun updateBusinessProcess(user: UserSession?, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.updateBusinessProcess("Bearer $token",
            UpdateBusinessProcessRequest(
                accountsDepartment = accountsDepartment,
                businessProcess = selectedBusinessProcess.value
            )
        )

        call.enqueue(object : Callback<UpdateBusinessProcessResponse> {
            override fun onResponse(call: Call<UpdateBusinessProcessResponse>, response: Response<UpdateBusinessProcessResponse>) {
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

            override fun onFailure(call: Call<UpdateBusinessProcessResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun runBusinessProcess(user: UserSession?, accountsDepartment: AccountsDepartment,
                           postRectangleAPIList: List<PostRectangleAPI>){
        val token = user?.token
        val call = apiService.runBusinessProcess("Bearer $token",
            RunBusinessProcessRequest(
                accountsDepartment = accountsDepartment,
                postRectangleAPIList = postRectangleAPIList,
                businessProcess = selectedBusinessProcess.value
            )
        )

        call.enqueue(object : Callback<RunBusinessProcessResponse> {
            override fun onResponse(call: Call<RunBusinessProcessResponse>, response: Response<RunBusinessProcessResponse>) {
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

            override fun onFailure(call: Call<RunBusinessProcessResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
}