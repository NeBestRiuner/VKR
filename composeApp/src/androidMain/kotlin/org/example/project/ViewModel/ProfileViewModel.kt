package org.example.project.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.GetProfileInfoResponse
import org.example.project.API.Data.UpdateProfileInfoRequest
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.ProfileInfo
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    var profileInfo: ProfileInfo? = null
    var nameState = mutableStateOf("")
    var surnameState = mutableStateOf("")
    var patronymicState =  mutableStateOf("")
    var loginState =  mutableStateOf("")
    var phoneNumberState =  mutableStateOf("")

    fun getProfileInfo(user: UserSession?){
        val token = user?.token
        val call = apiService.getProfileInfo("Bearer $token", User(user?.login ?: "",""))

        call.enqueue(object : Callback<GetProfileInfoResponse> {
            override fun onResponse(call: Call<GetProfileInfoResponse>, response: Response<GetProfileInfoResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            profileInfo = result.getProfileInfo?.profileInfo
                            nameState.value = result.getProfileInfo?.profileInfo?.name ?: ""
                            surnameState.value = result.getProfileInfo?.profileInfo?.surname ?: ""
                            patronymicState.value = result.getProfileInfo?.profileInfo?.patronymic ?: ""
                            phoneNumberState.value = result.getProfileInfo?.profileInfo?.phoneNumber ?: ""
                            loginState.value = result.getProfileInfo?.user?.login ?: ""
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetProfileInfoResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun postUpdateProfileInfo(user: UserSession? ){
        val token = user?.token
        val call = apiService.postUpdateProfileInfo("Bearer $token",
            UpdateProfileInfoRequest(
                user = User(loginState.value, user?.password ?: ""),
                profileInfo = ProfileInfo(name = nameState.value,
                    surname = surnameState.value,
                    patronymic = patronymicState.value,
                    phoneNumber = phoneNumberState.value)
            )
        )

        call.enqueue(object : Callback<GetProfileInfoResponse> {
            override fun onResponse(call: Call<GetProfileInfoResponse>, response: Response<GetProfileInfoResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="200"){
                            profileInfo = result.getProfileInfo?.profileInfo
                            nameState.value = result.getProfileInfo?.profileInfo?.name ?: ""
                            surnameState.value = result.getProfileInfo?.profileInfo?.surname ?: ""
                            patronymicState.value = result.getProfileInfo?.profileInfo?.patronymic ?: ""
                            phoneNumberState.value = result.getProfileInfo?.profileInfo?.phoneNumber ?: ""
                            loginState.value = result.getProfileInfo?.user?.login ?: ""
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<GetProfileInfoResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun updatePassword(oldPassword:String, newPassword:String, repeatPassword: String, user: UserSession?){
        if(newPassword == repeatPassword && ((user?.password ?: "") == oldPassword)){
            user?.password = newPassword
            postUpdateProfileInfo(user)
        }else{
            println("Пароль проверку на клиенте не прошёл")
        }
    }


}