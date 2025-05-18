package org.example.project.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateInviteCodeResponse
import org.example.project.API.Data.GetPermissionResponse
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.AccountsDepartment
import org.example.project.Model.Permission
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel : ViewModel(){

    val inviteUserSetting = mutableStateOf(false)
    val changeUserSettings = mutableStateOf(false)

    val createdInviteCode = mutableStateOf("")

    fun getPermission(user: UserSession, userWithPermission: User, accountsDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.getPermission("Bearer $token", userWithPermission, accountsDepartment)

        call.enqueue(object : Callback<GetPermissionResponse> {
            override fun onResponse(call: Call<GetPermissionResponse>, response: Response<GetPermissionResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            if(result.permissionList.contains(Permission("Выдавать права",
                                    "Пользователь с этой настройкой может выдавать и отбирать любые права у любого бухгалтера")
                            )){
                                changeUserSettings.value = true
                            }else{
                                changeUserSettings.value = false
                            }
                            if(result.permissionList.contains(Permission("Приглашать сотрудников",
                                    "Пользователь с этой настройкой может приглашать других пользователей в бухгалтерию")
                            )){
                                inviteUserSetting.value = true
                            }else{
                                inviteUserSetting.value  = false
                            }
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetPermissionResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun createInviteCode(user: UserSession?, accountDepartment: AccountsDepartment){
        val token = user?.token
        val call = apiService.createInviteCode("Bearer $token", accountDepartment)

        call.enqueue(object : Callback<CreateInviteCodeResponse> {
            override fun onResponse(call: Call<CreateInviteCodeResponse>, response: Response<CreateInviteCodeResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            createdInviteCode.value = result.inviteCode
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<CreateInviteCodeResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
}