package org.example.project.ViewModel

import android.nfc.Tag
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import org.example.project.API.Data.CreateUserResponse
import org.example.project.API.Data.GetProfileInfoResponse
import org.example.project.API.Data.LoginUserResponse
import org.example.project.API.Data.UpdateProfileInfoRequest
import org.example.project.API.RetrofitClient.apiService
import org.example.project.Model.ProfileInfo
import org.example.project.Model.User
import org.example.project.Model.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizeViewModel : ViewModel() {
    var user: UserSession? = null
    var profileInfo: ProfileInfo? = null
    var nameState = mutableStateOf("")
    var surnameState = mutableStateOf("")
    var patronymicState =  mutableStateOf("")
    var loginState =  mutableStateOf("")
    var phoneNumberState =  mutableStateOf("")




    fun sendCreateUser( login: String, password: String, onNavigateToEnter:()->Unit){
        val call = apiService.postCreateUser(User(login,password))
        call.enqueue(object : Callback<CreateUserResponse> {
            override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="success"){
                            onNavigateToEnter()
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    fun sendLoginUser(login:String, password:String, onNavigateToMainMenu:()->Unit){
        val call = apiService.getLoginUser(User(login,password))

        call.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call: Call<LoginUserResponse>, response: Response<LoginUserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="success"){
                            user = UserSession(login,password,result.authToken)
                            onNavigateToMainMenu()
                        }
                    }
                    // Обработка успешного ответа
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                // Обработка ошибки сети
            }
        })

    }
    fun getProfileInfo(user: UserSession?){
        val token = user?.token
        val call = apiService.getProfileInfo("Bearer $token",User(user?.login ?: "",""))

        call.enqueue(object : Callback<GetProfileInfoResponse> {
            override fun onResponse(call: Call<GetProfileInfoResponse>, response: Response<GetProfileInfoResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if(result.status=="success"){
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
                        if(result.status=="success"){
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
    fun updatePassword(oldPassword:String, newPassword:String, repeatPassword: String){
        if(newPassword == repeatPassword && ((user?.password ?: "") == oldPassword)){
            user?.password = newPassword
            postUpdateProfileInfo(user)
        }else{
            println("Пароль проверку на клиенте не прошёл")
        }
    }
}